package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.Logging
import scala.collection.JavaConverters._
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.util.geo.Postcode
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.obj.attr.{AttributeType, Attribute}
import com.sksamuel.elastic4s._
import ElasticDsl._
import com.sksamuel.elastic4s.FieldType._
import com.sksamuel.elastic4s.SearchType.QueryAndFetch
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.common.unit.DistanceUnit
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.search.facet.terms.TermsFacet
import org.elasticsearch.common.xcontent.XContentFactory
import javax.annotation.PreDestroy
import com.cloudray.scalapress.search.Facet
import scala.Some
import com.cloudray.scalapress.search.FacetTerm
import com.cloudray.scalapress.search.ObjectRef
import com.cloudray.scalapress.search.SearchResult
import scala.concurrent.Await

/** @author Stephen Samuel */
class ElasticSearchService extends SearchService with Logging {

  val DELETED = Item.STATUS_DELETED.toLowerCase
  val DISABLED = Item.STATUS_DISABLED.toLowerCase
  val LIVE = Item.STATUS_LIVE.toLowerCase

  val MAX_RESULTS_HARD_LIMIT = 1000
  val DEFAULT_MAX_RESULTS = 200

  val FIELD_ATTRIBUTE = "attribute_"
  val FIELD_ATTRIBUTE_SINGLE = "attribute_single_"
  val FIELD_TAGS = "tags"
  val FIELD_LOCATION = "location"
  val FIELD_STATUS = "status"
  val FIELD_NAME = "name"
  val FIELD_NAME_NOT_ANALYSED = "name_raw"
  val FIELD_FOLDERS = "folders"
  val FIELD_OBJECT_ID = "objectid"
  val FIELD_OBJECT_TYPE = "objectType"
  val FIELD_PRIORITIZED = "prioritized"
  val FIELD_HAS_IMAGE = "hasImage"

  val TIMEOUT = 5000
  val INDEX = "scalapress"
  val TYPE = "obj"

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
    fields.append(FIELD_OBJECT_ID typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_OBJECT_TYPE typed IntegerType index "not_analyzed" store true)
    fields.append(FIELD_NAME typed StringType analyzer WhitespaceAnalyzer store true)
    fields.append(FIELD_NAME_NOT_ANALYSED typed StringType index "not_analyzed" store true)
    fields.append(FIELD_TAGS typed StringType analyzer KeywordAnalyzer)
    fields.append(FIELD_PRIORITIZED typed IntegerType index "not_analyzed")
    fields.append("location" typed GeoPointType)
    attributes.foreach(attr => {
      val t = attr.attributeType match {
        case AttributeType.Numerical => DoubleType
        case AttributeType.Date | AttributeType.DateTime => LongType
        case _ => StringType
      }
      fields.append(FIELD_ATTRIBUTE + attr.id typed t index "not_analyzed")
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

  override def contains(_id: String): Boolean = {
    val resp = client.sync.execute {
      get id _id from INDEX -> TYPE
    }
    resp.isExists
  }

  def _attributeNormalize(value: String): String = value.replace("!", "").replace(" ", "_").replace("/", "_")
  def _normalize(value: String): String = value.replace("!", "").replace("/", "_").toLowerCase
  def _attributeRestore(value: String): String = value.replace("_", " ")

  override def index(obj: Item) = index(Seq(obj))
  override def index(objs: Seq[Item]) {
    require(objs != null)

    val executions = objs
      .filter(_.objectType != null)
      .map(obj => Option(obj.status).getOrElse(Item.STATUS_DELETED).toLowerCase match {

      case DELETED | DISABLED =>
        logger.debug("Creating delete operation [id={}]", obj.id)
        new DeleteByIdDefinition(INDEX, TYPE, obj.id.toString)

      case LIVE =>
        val _fields = ListBuffer[(String, Any)](
          FIELD_OBJECT_ID -> obj.id,
          FIELD_OBJECT_TYPE -> obj.objectType.id.toString,
          FIELD_NAME -> _normalize(obj.name),
          FIELD_NAME_NOT_ANALYSED -> obj.name,
          FIELD_STATUS -> obj.status,
          FIELD_PRIORITIZED -> (if (obj.prioritized) 1 else 0)
        )

        Option(obj.labels).foreach(tags => tags.split(",").foreach(tag => _fields.append(FIELD_TAGS -> tag)))

        val hasImage = obj.images.size > 0
        _fields.append(FIELD_HAS_IMAGE -> hasImage.toString)

        obj.folders.asScala.foreach(folder => _fields.append(FIELD_FOLDERS -> folder.id))

        obj.attributeValues.asScala
          .filterNot(_.value == null)
          .filterNot(_.value.isEmpty)
          .foreach(av => {
          _fields.append(FIELD_ATTRIBUTE + av.attribute.id.toString -> _attributeNormalize(av.value))
          _fields.append("has_attribute_" + av.attribute.id.toString -> "1")
          if (av.attribute.attributeType == AttributeType.Postcode) {
            Postcode.gps(av.value).foreach(gps => {
              _fields.append(FIELD_LOCATION -> gps.string())
            })
          }
        })

        insert into INDEX -> TYPE id obj.id fields _fields
    })

    import scala.concurrent.duration._
    Await.ready(client.bulk(executions: _*), 1 minute)
  }

  override def remove(_id: String) {
    client.sync.delete {
      s"$INDEX/$TYPE" -> _id
    }
  }

  override def typeahead(q: String, limit: Int): Seq[ObjectRef] = {
    val resp = client.sync.execute {
      select in INDEX -> TYPE prefix FIELD_NAME -> q
        .toLowerCase size limit searchType QueryAndFetch
    }
    _resp2ref(resp)
  }

  override def count: Long = {
    client.sync.execute {
      countall from INDEX
    }.getCount
  }

  def _count(search: SavedSearch): Long = {
    client.sync.execute {
      countall from INDEX query {
        _query(search)
      }
    }.getCount
  }

  override def search(search: SavedSearch): SearchResult = {
    val resp = _search(search)
    val refs = _resp2ref(resp)
    val facets = _resp2facets(resp, Option(search.objectType).map(_.attributes.asScala).getOrElse(Nil))
    val count = _count(search)
    logger.debug("Search returned {} refs", refs.size)
    SearchResult(refs, facets, count)
  }

  def _maxResults(search: SavedSearch) =
    if (search.maxResults < 1) DEFAULT_MAX_RESULTS
    else if (search.maxResults > MAX_RESULTS_HARD_LIMIT) MAX_RESULTS_HARD_LIMIT
    else search.maxResults

  def _query(search: SavedSearch): QueryDefinition = {

    val queries = new ListBuffer[QueryDefinition]
    val filters = new ListBuffer[FilterDefinition]

    Option(search.name).orElse(Option(search.keywords))
      .map(_.replace("+", " ").replace("\\", "").trim)
      .filterNot(_.isEmpty)
      .foreach(_
      .split(" ")
      .filterNot(_.isEmpty)
      .map(value => _normalize(value))
      .foreach(value => queries.append(field("name", value)))
    )

    Option(search.objectType).map(_.id.toString).foreach(id => filters.append(termFilter(FIELD_OBJECT_TYPE, id)))

    Option(search.labels).map(labels =>
      labels.split(",")
        .filterNot(_.isEmpty)
        .filterNot(_.toLowerCase == "random")
        .filterNot(_.toLowerCase == "latest")
        .foreach(tag => filters.append(termFilter(FIELD_TAGS, tag))))

    Option(search.searchFolders)
      .filter(_.trim.length > 0)
      .map(_.replaceAll("\\D", ""))
      .foreach(_.split(",").foreach(f => filters.append(termFilter(FIELD_FOLDERS, f))))

    search.attributeValues.asScala
      .filter(_.value.trim.length > 0)
      .filterNot(_.value.trim.toLowerCase == "any")
      .foreach(av => queries.append(field(_attrField(av.attribute.id), _attributeNormalize(av.value))))

    Option(search.hasAttributes)
      .filter(_.trim.length > 0)
      .foreach(arg => filters.append(termFilter("has_attribute_" + arg, "1")))

    Option(search.ignorePast)
      .foreach(attr => filters.append(numericRangeFilter(_attrField(attr.id)).gte(System.currentTimeMillis())))

    if (search.imageOnly) filters.append(termFilter(FIELD_HAS_IMAGE, "true"))

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

  def _search(search: SavedSearch): SearchResponse = {

    val filter = Option(search.location).flatMap(Postcode.gps).map(gps => {
      geoDistance(FIELD_LOCATION).point(gps.lat, gps.lon).distance(search.distance, DistanceUnit.MILES)
    })

    val query = filter match {
      case None => _query(search)
      case Some(f) => filteredQuery.query(_query(search)).filter(f)
    }

    val limit = _maxResults(search)

    val triggeredFacets = search.attributeValues.asScala.map(_.attribute.id.toString).toSeq
    val filteredFacets = search.facets.filterNot(facet => triggeredFacets.contains(facet))

    val facets = filteredFacets.map {
      case id if id.forall(_.isDigit) => facet terms id fields FIELD_ATTRIBUTE + id size 20
      case name => facet terms name fields name
    }

    val prioritized = by field FIELD_PRIORITIZED order SortOrder.DESC
    val sort = _sort(search)

    client.sync.execute {
      select in INDEX -> TYPE searchType QueryAndFetch from (search.pageNumber - 1) * limit size limit sort(
        prioritized,
        sort
        ) query query facets facets
    }
  }

  def _attrField(obj: Any) = FIELD_ATTRIBUTE + obj.toString

  def _sort(search: SavedSearch) = search.sortType match {

    case Sort.Random => by script "Math.random()" as "number" order SortOrder.ASC
    case Sort.Attribute if search.sortAttribute != null =>
      by field _attrField(search.sortAttribute.id) order SortOrder.ASC missing "_last"
    case Sort.AttributeDesc if search.sortAttribute != null =>
      by field _attrField(search.sortAttribute.id) order SortOrder.DESC missing "_last"
    case Sort.Name => by field FIELD_NAME_NOT_ANALYSED order SortOrder.ASC
    case Sort.Oldest => by field FIELD_OBJECT_ID order SortOrder.ASC
    case _ => by field FIELD_OBJECT_ID order SortOrder.DESC
  }

  def _resp2facets(resp: SearchResponse,
                   attributes: Iterable[Attribute]): Seq[com.cloudray.scalapress.search.Facet] = {
    Option(resp.getFacets) match {
      case None => Nil
      case Some(facets) =>
        facets.facets().asScala
          .filter(_.isInstanceOf[TermsFacet])
          .map(_.asInstanceOf[TermsFacet])
          .map(facet => {
          val terms = facet.getEntries.asScala
            .map(entry => FacetTerm(_attributeRestore(entry.getTerm.string()), entry.getCount)).toSeq
          val name = facet.getName match {
            case id if id.forall(_.isDigit) => attributes.find(_.id.toString == id).map(_.name).getOrElse("error")
            case n => n
          }
          Facet(name, facet.getName, terms)
        })
    }
  }

  def _resp2ref(resp: SearchResponse): Seq[ObjectRef] = {
    resp.getHits.asScala.map(arg => {
      val id = arg.id.toLong
      val objectType = arg.getSource.get(FIELD_OBJECT_TYPE).toString.toLong
      val prioritized = arg.getSource.get(FIELD_PRIORITIZED) == 1 || arg.getSource.get(FIELD_PRIORITIZED) == "1"
      val n = arg.getSource.get(FIELD_NAME_NOT_ANALYSED).toString
      val status = arg.getSource.get(FIELD_STATUS).toString
      val attributes = arg.getSource.asScala.filter(_._2 != null).filter(_._1.startsWith(FIELD_ATTRIBUTE))
        .map(field => {
        val id = field._1.drop(FIELD_ATTRIBUTE.length).toLong
        val value = _attributeRestore(field._2.toString)
        (id, value)
      }).toMap
      new ObjectRef(id, objectType, n, status, attributes, Nil, prioritized)
    }).toSeq
  }

  def _source(obj: Item) = {
    require(obj.id > 0)

    val json = XContentFactory
      .jsonBuilder()
      .startObject()
      .field(FIELD_OBJECT_ID, obj.id)
      .field(FIELD_OBJECT_TYPE, obj.objectType.id.toString)
      .field(FIELD_NAME, _normalize(obj.name))
      .field(FIELD_NAME_NOT_ANALYSED, obj.name)
      .field(FIELD_STATUS, obj.status)

    Option(obj.labels)
      .foreach(tags => tags
      .split(",")
      .foreach(tag => json.field(FIELD_TAGS, tag)))

    val hasImage = obj.images.size > 0
    json.field(FIELD_HAS_IMAGE, hasImage.toString)

    val folderIds = obj.folders.asScala.map(_.id.toString)
    json.field(FIELD_FOLDERS, folderIds.toSeq: _*)

    obj.attributeValues.asScala
      .filterNot(_.value == null)
      .filterNot(_.value.isEmpty)
      .foreach(av => {
      json
        .field(FIELD_ATTRIBUTE + av.attribute.id.toString,
        _attributeNormalize(av.value))
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


