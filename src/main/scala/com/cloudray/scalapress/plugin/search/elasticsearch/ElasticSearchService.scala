package com.cloudray.scalapress.plugin.search.elasticsearch

import scala.collection.JavaConverters._
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.item.attr.{AttributeDao, AttributeType, Attribute}
import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.FieldType._
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.facet.terms.TermsFacet
import com.cloudray.scalapress.framework.Logging
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.Facet
import scala.Some
import com.cloudray.scalapress.search.FacetTerm
import javax.annotation.PreDestroy
import com.cloudray.scalapress.search.AttributeFacetField
import com.cloudray.scalapress.search.ItemRef
import com.cloudray.scalapress.search.SearchResult
import scala.concurrent.Await

/** @author Stephen Samuel */
@Autowired
class ElasticSearchService(attributeDao: AttributeDao)
  extends IndexedSearchService with ElasticsearchUtils with ElasticsearchStats with Logging {

  var setup = false

  val tempDir = File.createTempFile("findingtemp", "tmp").getParent
  val dataDir = new File(tempDir + "/elasticsearch-data-" + UUID.randomUUID().toString)
  dataDir.mkdir()
  dataDir.deleteOnExit()
  logger.info("Setting ES data dir [{}]", dataDir)

  val settings = ImmutableSettings.settingsBuilder()
    .put("node.http.enabled", false)
    .put("http.enabled", false)
    .put("path.data", dataDir.getAbsolutePath)
    .put("index.number_of_shards", 1)
    .put("index.number_of_replicas", 0)
    .put("index.refresh_interval", 10)
    .put("indices.memory.min_shard_index_buffer_size", "1mb")
    .put("indices.memory.index_buffer_size", "5%")
    .put("indices.cache.filter.size", "4mb")
    .put("indices.min_index_buffer_size", "4mb")
    .put("cluster.name", "scalapress-" + UUID.randomUUID.toString.substring(0, 8))
    .build

  val client = ElasticClient.local(settings)

  def setupIndex(attributes: Seq[Attribute]) {

    val fields = new ListBuffer[FieldDefinition]
    fields.append(id typed StringType index "not_analyzed" store true)
    fields.append(FIELD_FOLDERS typed StringType index "not_analyzed" store true)
    fields.append(FIELD_ITEM_ID typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_ITEM_TYPE_ID typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_NAME typed StringType analyzer WhitespaceAnalyzer store true)
    fields.append(FIELD_NAME_NOT_ANALYSED typed StringType index "not_analyzed" store true)
    fields.append(FIELD_TAGS typed StringType analyzer KeywordAnalyzer)
    fields.append(FIELD_PRIORITIZED typed IntegerType index "not_analyzed")
    fields.append(FIELD_LOCATION typed GeoPointType)
    fields.append(FIELD_PRICE typed IntegerType)

    attributes.foreach(attribute => {
      val t = attribute.attributeType match {
        case AttributeType.Numerical => DoubleType
        case AttributeType.Date | AttributeType.DateTime => LongType
        case _ => StringType
      }
      fields.append(attributeField(attribute) typed t index "not_analyzed")
    })

    logger.debug("Creating INDEX {}->{}", INDEX, TYPE)
    client.sync.execute {
      create index INDEX replicas 0 shards 1 mappings {
        TYPE source true as (fields.toList: _*)
      }

      //      analysis {
      //      CustomAnalyzerDefinition("letterDigitAnalyzer", LetterTokenizer, LowercaseTokenFilter)
      //  }
    }
  }

  override def index(item: Item) = index(Seq(item))
  override def index(items: Seq[Item]) {
    val executions = ElasticsearchIndexBuilder.build(items)
    import scala.concurrent.duration._
    Await.ready(client.bulk(executions: _*), 1 minute)
  }

  override def exists(_id: String): Boolean = {
    val resp = client.sync.execute {
      get id _id from INDEX -> TYPE
    }
    resp.isExists
  }

  override def remove(_id: String) {
    client.sync.delete {
      s"$INDEX/$TYPE" -> _id
    }
  }

  override def count: Long = {
    client.sync.execute {
      countall from INDEX
    }.getCount
  }

  def _count(search: Search): Long = {
    client.sync.execute {
      ElasticsearchCountBuilder.build(search)
    }.getCount
  }

  override def search(search: Search): SearchResult = {
    val resp = _search(search)
    val refs = _resp2ref(resp)
    val facets = resp2facets(resp)
    val count = _count(search)
    logger.debug("Search returned {} refs", refs.size)
    SearchResult(refs, facets, count)
  }

  def _search(search: Search): SearchResponse = {
    client.sync.execute {
      ElasticsearchSearchBuilder.build(search)
    }
  }

  def resp2facets(resp: SearchResponse): Seq[Facet] = {
    Option(resp.getFacets) match {
      case None => Nil
      case Some(facets) => {
        val attributes = attributeDao.findAll
        val termFacets = facets.asScala.filter(_.isInstanceOf[TermsFacet]).map(_.asInstanceOf[TermsFacet])
        termFacets.map(facet => {
          val name = attributes.filter(_.id == facet.getName.toLong).head.name
          val field = AttributeFacetField(attributes.filter(_.id == facet.getName.toLong).head.id)
          val terms = facetTerms(facet)
          Facet(name, field, terms)
        }).toSeq
      }
    }
  }

  def facetTerms(facet: TermsFacet): Seq[FacetTerm] = {
    facet.getEntries.asScala
      .map(entry => FacetTerm(attributeUnescape(entry.getTerm.string()), entry.getCount))
      .toSeq
  }

  def _resp2ref(resp: SearchResponse): Seq[ItemRef] = {
    resp.getHits.asScala.map(arg => {
      val id = arg.id.toLong
      val itemTypeId = arg.getSource.get(FIELD_ITEM_TYPE_ID).toString.toLong
      val prioritized = arg.getSource.get(FIELD_PRIORITIZED) == "1"
      val n = arg.getSource.get(FIELD_NAME_NOT_ANALYSED).toString
      val status = arg.getSource.get(FIELD_STATUS).toString
      val attributes = arg.getSource.asScala.filter(_._2 != null).filter(_._1.startsWith(FIELD_ATTRIBUTE))
        .map(field => {
        val id = field._1.drop(FIELD_ATTRIBUTE.length).toLong
        val value = attributeUnescape(field._2.toString)
        (id, value)
      }).toMap
      new ItemRef(id, itemTypeId, n, status, attributes, Nil, prioritized)
    }).toSeq
  }

  @PreDestroy
  def shutdown() {
    client.close()
    dataDir.delete()
  }

}


