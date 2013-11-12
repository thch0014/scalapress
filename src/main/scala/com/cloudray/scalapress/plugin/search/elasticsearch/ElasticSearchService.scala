package com.cloudray.scalapress.plugin.search.elasticsearch

import scala.collection.JavaConverters._
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.util.geo.Postcode
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.item.attr.{AttributeDao, AttributeType, Attribute}
import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.FieldType._
import com.sksamuel.elastic4s.SearchType.QueryAndFetch
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.common.unit.DistanceUnit
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.search.facet.terms.TermsFacet
import scala.concurrent.Await
import com.cloudray.scalapress.framework.Logging
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.Facet
import scala.Some
import com.cloudray.scalapress.search.FacetTerm
import org.elasticsearch.common.xcontent.XContentFactory
import javax.annotation.PreDestroy
import com.cloudray.scalapress.search.AttributeFacetField
import com.cloudray.scalapress.search.ItemRef
import com.cloudray.scalapress.search.SearchResult

/** @author Stephen Samuel */
@Autowired
class ElasticSearchService(attributeDao: AttributeDao) extends IndexedSearchService with Logging {

  val DELETED = Item.STATUS_DELETED.toLowerCase
  val DISABLED = Item.STATUS_DISABLED.toLowerCase
  val LIVE = Item.STATUS_LIVE.toLowerCase

  val MAX_RESULTS_HARD_LIMIT = 1000
  val DEFAULT_MAX_RESULTS = 200

  val FIELD_ATTRIBUTE_SINGLE = "attribute_single_"
  val FIELD_TAGS = "tags"
  val FIELD_LOCATION = "location"
  val FIELD_STATUS = "status"
  val FIELD_NAME = "name"
  val FIELD_NAME_NOT_ANALYSED = "name_raw"
  val FIELD_FOLDERS = "folders"
  val FIELD_ITEM_ID = "itemid"
  val FIELD_ITEM_TYPE_ID = "itemType"
  val FIELD_PRIORITIZED = "prioritized"
  val FIELD_HAS_IMAGE = "hasImage"

  val TIMEOUT = 5000
  val INDEX = "scalapress"
  val TYPE = "item"

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
    fields.append(FIELD_ITEM_ID typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_ITEM_TYPE_ID typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_NAME typed StringType analyzer WhitespaceAnalyzer store true)
    fields.append(FIELD_NAME_NOT_ANALYSED typed StringType index "not_analyzed" store true)
    fields.append(FIELD_TAGS typed StringType analyzer KeywordAnalyzer)
    fields.append(FIELD_PRIORITIZED typed IntegerType index "not_analyzed")
    fields.append("location" typed GeoPointType)
    attributes.foreach(attribute => {
      val t = attribute.attributeType match {
        case AttributeType.Numerical => DoubleType
        case AttributeType.Date | AttributeType.DateTime => LongType
        case _ => StringType
      }
      fields.append(attrField(attribute) typed t index "not_analyzed")
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

  override def exists(_id: String): Boolean = {
    val resp = client.sync.execute {
      get id _id from INDEX -> TYPE
    }
    resp.isExists
  }

  def attributeNormalize(value: String): String = value.replace("!", "").replace(" ", "_").replace("/", "_")
  def _normalize(value: String): String = value.replace("!", "").replace("/", "_").toLowerCase
  def _attributeUnescape(value: String): String = value.replace("_", " ")

  override def index(item: Item) = index(Seq(item))
  override def index(items: Seq[Item]) {
    require(items != null)

    val executions = items
      .filter(_.itemType != null)
      .map(item => Option(item.status).getOrElse(Item.STATUS_DELETED).toLowerCase match {

      case DELETED | DISABLED =>
        logger.debug("Creating delete operation [id={}]", item.id)
        new DeleteByIdDefinition(INDEX, TYPE, item.id.toString)

      case LIVE =>
        val _fields = ListBuffer[(String, Any)](
          FIELD_ITEM_ID -> item.id,
          FIELD_ITEM_TYPE_ID -> item.itemType.id.toString,
          FIELD_NAME -> _normalize(item.name),
          FIELD_NAME_NOT_ANALYSED -> item.name,
          FIELD_STATUS -> item.status,
          FIELD_PRIORITIZED -> (if (item.prioritized) 1 else 0)
        )

        Option(item.labels).foreach(tags => tags.split(",").foreach(tag => _fields.append(FIELD_TAGS -> tag)))

        val hasImage = item.images.size > 0
        _fields.append(FIELD_HAS_IMAGE -> hasImage.toString)

        item.folders.asScala.foreach(folder => _fields.append(FIELD_FOLDERS -> folder.id))

        item.attributeValues.asScala
          .filterNot(_.value == null)
          .filterNot(_.value.isEmpty)
          .foreach(av => {
          _fields.append(attrField(av.attribute) -> attributeNormalize(av.value))
          _fields.append("has_attribute_" + av.attribute.id.toString -> "1")
          if (av.attribute.attributeType == AttributeType.Postcode) {
            Postcode.gps(av.value).foreach(gps => {
              _fields.append(FIELD_LOCATION -> gps.string())
            })
          }
        })

        insert into INDEX -> TYPE id item.id fields _fields
    })

    import scala.concurrent.duration._
    Await.ready(client.bulk(executions: _*), 1 minute)
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
      countall from INDEX query {
        _query(search)
      }
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

  def _maxResults(search: Search) =
    if (search.maxResults < 1) DEFAULT_MAX_RESULTS
    else if (search.maxResults > MAX_RESULTS_HARD_LIMIT) MAX_RESULTS_HARD_LIMIT
    else search.maxResults

  def _query(search: Search): QueryDefinition = {

    val queries = new ListBuffer[QueryDefinition]
    val filters = new ListBuffer[FilterDefinition]

    search.itemTypeId.map(termQuery(FIELD_ITEM_TYPE_ID, _)).foreach(queries append _)
    search.folders.map(termQuery(FIELD_FOLDERS, _)).foreach(queries append _)

    search.name
      .map(_.replace("+", " ").replace("\\", "").trim)
      .filterNot(_.isEmpty)
      .foreach(_
      .split(" ")
      .filterNot(_.isEmpty)
      .map(value => _normalize(value))
      .foreach(value => queries.append(field("name", value)))
    )

    search.attributeValues
      .filterNot(_.value.trim.toLowerCase == "any")
      .map(av => termFilter(attrField(av.attribute), attributeNormalize(av.value)))
      .foreach(filters append _)

    search.hasAttributes.foreach(arg => filters.append(termFilter("has_attribute_" + arg, "1")))

    search.tags
      .filterNot(_.isEmpty)
      .filterNot(_.toLowerCase == "random")
      .filterNot(_.toLowerCase == "latest")
      .foreach(tag => filters.append(termFilter(FIELD_TAGS, tag)))

    search.ignorePast
      .foreach(attr => filters.append(numericRangeFilter(attrField(attr)).gte(System.currentTimeMillis())))

    if (search.imagesOnly) filters.append(termFilter(FIELD_HAS_IMAGE, "true"))

    //    search.selectedFacets.foreach(facet => facet.field match {
    //      case AttributeFacetField(id) =>
    //        queries.append(termFilter(_attrField(id), _attributeNormalize(facet.value)))
    //      case TagsFacetField =>
    //      case _ =>
    //    })

    val q = queries.size match {
      case 0 => query("*:*")
      case _ => bool(must(queries: _*))
    }
    val filter = filters.size match {
      case 0 => matchAllFilter
      case _ => bool(must(filters: _*))
    }

    filteredQuery.filter(filter).query(q)
  }

  def _search(search: Search): SearchResponse = {

    val filter = search.location.flatMap(Postcode.gps).map(gps => {
      geoDistance(FIELD_LOCATION).point(gps.lat, gps.lon).distance(search.distance, DistanceUnit.MILES)
    })

    val query = filter match {
      case None => _query(search)
      case Some(f) => filteredQuery.query(_query(search)).filter(f)
    }

    val limit = _maxResults(search)

    // val triggeredFacets = search.attributeValues.asScala.map(_.attribute.id.toString).toSeq
    //    val filteredFacets = search.facets.filterNot(facet => triggeredFacets.contains(facet))

    val facets = Nil
    //    val facets = filteredFacets.map {
    //    case AttributeFacetField(id) => facet terms id.toString fields attrField(id) size 20
    //  }

    val prioritized = by field FIELD_PRIORITIZED order SortOrder.DESC
    val sort = _sort(search)

    client.sync.execute {
      select in INDEX -> TYPE searchType QueryAndFetch from (search.pageNumber - 1) * limit size limit sort(
        prioritized,
        sort
        ) query query facets facets
    }
  }

  val FIELD_ATTRIBUTE = "attribute_"
  def attrField(attribute: Attribute): String = {
    FIELD_ATTRIBUTE + attribute.id.toString
  }

  def _sort(search: Search) = search.sort match {

    case Sort.Random => by script "Math.random()" as "number" order SortOrder.ASC
    case Sort.Attribute if search.sortAttribute.isDefined =>
      by field attrField(search.sortAttribute.get) order SortOrder.ASC missing "_last"
    case Sort.AttributeDesc if search.sortAttribute.isDefined =>
      by field attrField(search.sortAttribute.get) order SortOrder.DESC missing "_last"
    case Sort.Name => by field FIELD_NAME_NOT_ANALYSED order SortOrder.ASC
    case Sort.Oldest => by field FIELD_ITEM_ID order SortOrder.ASC
    case _ => by field FIELD_ITEM_ID order SortOrder.DESC
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
      .map(entry => FacetTerm(_attributeUnescape(entry.getTerm.string()), entry.getCount))
      .toSeq
  }

  def _resp2ref(resp: SearchResponse): Seq[ItemRef] = {
    resp.getHits.asScala.map(arg => {
      val id = arg.id.toLong
      val itemTypeId = arg.getSource.get(FIELD_ITEM_TYPE_ID).toString.toLong
      val prioritized = arg.getSource.get(FIELD_PRIORITIZED) == 1 || arg.getSource.get(FIELD_PRIORITIZED) == "1"
      val n = arg.getSource.get(FIELD_NAME_NOT_ANALYSED).toString
      val status = arg.getSource.get(FIELD_STATUS).toString
      val attributes = arg.getSource.asScala.filter(_._2 != null).filter(_._1.startsWith(FIELD_ATTRIBUTE))
        .map(field => {
        val id = field._1.drop(FIELD_ATTRIBUTE.length).toLong
        val value = _attributeUnescape(field._2.toString)
        (id, value)
      }).toMap
      new ItemRef(id, itemTypeId, n, status, attributes, Nil, prioritized)
    }).toSeq
  }

  def _source(item: Item) = {
    require(item.id > 0)

    val json = XContentFactory
      .jsonBuilder()
      .startObject()
      .field(FIELD_ITEM_ID, item.id)
      .field(FIELD_ITEM_TYPE_ID, item.itemType.id.toString)
      .field(FIELD_NAME, _normalize(item.name))
      .field(FIELD_NAME_NOT_ANALYSED, item.name)
      .field(FIELD_STATUS, item.status)

    Option(item.labels)
      .foreach(tags => tags
      .split(",")
      .foreach(tag => json.field(FIELD_TAGS, tag)))

    val hasImage = item.images.size > 0
    json.field(FIELD_HAS_IMAGE, hasImage.toString)

    val folderIds = item.folders.asScala.map(_.id.toString)
    json.field(FIELD_FOLDERS, folderIds.toSeq: _*)

    item.attributeValues.asScala
      .filterNot(_.value == null)
      .filterNot(_.value.isEmpty)
      .foreach(av => {
      json.field(attrField(av.attribute), attributeNormalize(av.value))
      json.field("has_attribute_" + av.attribute.id.toString, "1")
      if (av.attribute.attributeType == AttributeType.Postcode) {
        Postcode.gps(av.value).foreach(gps => {
          json.field(FIELD_LOCATION, gps.string())
        })
      }
    })

    json.endObject()
  }

  @PreDestroy
  def shutdown() {
    client.close()
    dataDir.delete()
  }

  def stats: Map[String, String] = {
    val nodes = client.admin.cluster().prepareNodesStats().all().execute().actionGet(TIMEOUT).getNodes
    val map = scala.collection.mutable.Map[String, String]()
    nodes.flatMap(node => {
      map.put("jvm.mem.heapUsed", node.getJvm.mem.heapUsed.mb + "mb")
      map.put("jvm.mem.heapCommitted", node.getJvm.mem.heapCommitted.mb + "mb")
      map
        .put("jvm.mem.nonHeapCommitted",
        node.getJvm.mem.nonHeapCommitted.mb + "mb")
      map.put("jvm.mem.nonHeapUsed", node.getJvm.mem.nonHeapUsed.mb + "mb")
      map.put("jvm.threads.count", node.getJvm.threads.count.toString)
      map.put("jvm.threads.peakCount", node.getJvm.threads.peakCount.toString)

      map.put("indices.docs.count", node.getIndices.getDocs.getCount.toString)
      map
        .put("indices.docs.deleted",
        node.getIndices.getDocs.getDeleted.toString)

      map.put("indices.store.size", node.getIndices.getStore.size.mb + "mb")
      map
        .put("indices.store.throttleTime",
        node.getIndices.getStore.throttleTime.toString)

      map.put("transport.txCount", node.getTransport.txCount.toString)
      map.put("transport.txSize", node.getTransport.txSize.toString)
      map.put("transport.txSize", node.getTransport.txSize.toString)

      node.getThreadPool.iterator().asScala.foreach(pool => {
        map.put("threadpool." + pool.getName + ".active", pool.getActive.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getCompleted.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getLargest.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getQueue.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getRejected.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getThreads.toString)
      })

      var k = 1
      node.getFs.iterator().asScala.foreach(fs => {
        map.put("threadpool.fs" + k + ".available", fs.getAvailable.toString)
        map.put("threadpool.fs" + k + ".diskQueue", fs.getDiskQueue.toString)
        map.put("threadpool.fs" + k + ".diskReads", fs.getDiskReads.toString)
        map.put("threadpool.fs" + k + ".diskWrites", fs.getDiskWrites.toString)
        map.put("threadpool.fs" + k + ".free", fs.getFree.toString)
        map.put("threadpool.fs" + k + ".total", fs.getTotal.toString)
        k = k + 1
      })

      map.map(arg => ("search." + node.getHostname + "." + arg._1, arg._2))

    }).toMap
  }

}


