package com.cloudray.scalapress.plugin.search.elasticsearch

import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.SearchResponse
import com.cloudray.scalapress.Logging
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import org.elasticsearch.index.query._
import scala.collection.mutable.{ListBuffer, ArrayBuffer}
import scala.Option
import com.cloudray.scalapress.enums.{AttributeType, Sort}
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.util.geo.Postcode
import org.elasticsearch.common.unit.DistanceUnit
import javax.annotation.PreDestroy
import com.cloudray.scalapress.search._
import org.elasticsearch.search.facet.terms.TermsFacet
import scala.Some
import com.cloudray.scalapress.search.ObjectRef
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.obj.attr.Attribute
import com.sksamuel.elastic4s.{ElasticDsl, ElasticClient}
import ElasticDsl._
import com.sksamuel.elastic4s.FieldType.{StringType, GeoPointType, IntegerType}
import com.sksamuel.elastic4s.SearchType.QueryAndFetch

/** @author Stephen Samuel */

@Component
class ElasticSearchService extends SearchService with Logging {

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

    val TIMEOUT = 5000
    val INDEX = "scalapress"
    val TYPE = "obj"

    var setup = false

    val tempDir = File.createTempFile("findingfemp", "tmp").getParent
    val dataDir = new File(tempDir + "/" + UUID.randomUUID().toString)
    dataDir.mkdir()
    dataDir.deleteOnExit()
    logger.info("Setting ES data dir [{}]", dataDir)

    val settings = ImmutableSettings.settingsBuilder()
      .put("node.http.enabled", false)
      .put("http.enabled", false)
      .put("indices.cache.filter.size", "8mb")
      //  .put("index.gateway.type", "none")
      //    .put("gateway.type", "none")
      //   .put("index.store.type", "memory")
      .put("path.data", dataDir.getAbsolutePath)
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 0)
      .put("indices.memory.min_shard_index_buffer_size", "1mb")
      .put("indices.memory.index_buffer_size", "10%")
      .put("min_index_buffer_size", "4mb")
      .build

    val client = ElasticClient.local(settings)

    def setupIndex(attributes: Seq[Attribute]) {

        val fields = new ListBuffer[FieldDefinition]
        fields.append(id typed StringType index "not_analyzed" store true)
        fields.append("objectid" typed IntegerType index "not_analyzed" store true)
        fields.append("objectType" typed IntegerType index "not_analyzed" store true)
        fields.append(FIELD_NAME_NOT_ANALYSED typed StringType index "not_analyzed" store true)
        fields.append(FIELD_TAGS typed StringType index "not_analyzed")
        fields.append("location" typed GeoPointType)
        attributes.foreach(attr => fields.append(FIELD_ATTRIBUTE + attr.id fieldType StringType index "not_analyzed"))

        client.execute {
            create index INDEX mappings {
                TYPE source true as (fields.toList: _*)
            }
        }
    }

    override def contains(_id: String): Boolean = {
        val resp = client.sync.execute {
            get id _id from INDEX -> TYPE
        }
        resp.isExists
    }

    def _attributeNormalize(value: String): String = value.replace("!", "").replace(" ", "_")
    def _normalize(value: String): String = value.replace("!", "").toLowerCase
    def _attributeRestore(value: String): String = value.replace("_", " ")

    override def index(obj: Obj) {
        logger.debug("Indexing [{}, {}]", obj.id, obj.name)

        val _fields = ListBuffer[(String, Any)]("objectid" -> obj.id,
            "objectType" -> obj.objectType.id.toString,
            FIELD_NAME -> _normalize(obj.name),
            FIELD_NAME_NOT_ANALYSED -> obj.name,
            FIELD_STATUS -> obj.status)

        Option(obj.labels).foreach(tags => tags.split(",").foreach(tag => _fields append FIELD_TAGS -> tag))

        val hasImage = obj.images.size > 0
        _fields.append("hasImage" -> hasImage.toString)

        obj.folders.asScala.foreach(folder => _fields.append(FIELD_FOLDERS -> folder.toString))

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

        client execute {
            insert into INDEX -> TYPE id obj.id fields _fields
        }
    }

    override def remove(_id: String) {
        client.execute {
            delete id _id from INDEX -> TYPE
        }
    }

    override def typeahead(q: String, limit: Int): Seq[ObjectRef] = {
        val resp = client.sync.execute {
            select in INDEX -> TYPE prefix FIELD_NAME -> q.toLowerCase size limit searchType QueryAndFetch
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
            countall from INDEX query2 {
                _buildQuery(search)
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

    def _buildQuery(search: SavedSearch): QueryStringQueryBuilder = {
        val queryString = _buildQueryString(search)
        queryString.length match {
            case 0 => new QueryStringQueryBuilder("*:*")
            case _ => new QueryStringQueryBuilder(queryString).defaultOperator(QueryStringQueryBuilder.Operator.AND)
        }
    }

    def _maxResults(search: SavedSearch) =
        if (search.maxResults < 1) DEFAULT_MAX_RESULTS
        else if (search.maxResults > MAX_RESULTS_HARD_LIMIT) MAX_RESULTS_HARD_LIMIT
        else search.maxResults

    def _buildQueryString(search: SavedSearch): String = {

        val buffer = new ArrayBuffer[String]()

        Option(search.name)
          .orElse(Option(search.keywords))
          .filter(_.trim.length > 0)
          .foreach(name => name
          .trim
          .split(" ")
          .filter(_.trim.length > 0)
          .map(value => _normalize(value))
          .foreach(arg => buffer.append(s"name:$arg")))

        Option(search.objectType).foreach(arg => buffer.append("objectType:" + arg.id.toString))

        Option(search.labels) match {
            case Some(labels) =>
                labels.split(",")
                  .filterNot(_.isEmpty).filterNot(_.toLowerCase == "random").filterNot(_.toLowerCase == "latest")
                  .foreach(tag => buffer.append(FIELD_TAGS + ":\"" + tag + "\""))
            case _ =>
        }

        Option(search.searchFolders)
          .filter(_.trim.length > 0)
          .map(_.replaceAll("\\D", ""))
          .foreach(_.split(",").foreach(f => buffer.append(FIELD_FOLDERS + ":" + f)))

        search.attributeValues.asScala.filter(_.value.trim.length > 0).foreach(av => {
            buffer.append(FIELD_ATTRIBUTE + av.attribute.id + ":" + _attributeNormalize(av.value))
        })

        Option(search.hasAttributes)
          .filter(_.trim.length > 0)
          .foreach(arg => {
            buffer.append("has_attribute_" + arg + ":1")
        })

        if (search.imageOnly)
            buffer.append("hasImage:true")

        buffer.mkString(" ")
    }

    def _search(search: SavedSearch): SearchResponse = {

        val filter = Option(search.location)
          .flatMap(Postcode.gps(_))
          .map(gps => {
            new GeoDistanceFilterBuilder(FIELD_LOCATION)
              .point(gps.lat, gps.lon)
              .distance(search.distance, DistanceUnit.MILES)
        })

        val query = filter match {
            case None => _buildQuery(search)
            case Some(f) => new FilteredQueryBuilder(_buildQuery(search), f)
        }

        val limit = _maxResults(search)

        val triggeredFacets = search.attributeValues.asScala.map(_.attribute.id.toString).toSeq
        val filteredFacets = search.facets.filterNot(facet => triggeredFacets.contains(facet))

        val facets = filteredFacets.map(_ match {
            case id if id.forall(_.isDigit) => facet terms id fields FIELD_ATTRIBUTE + id size 20
            case name => facet terms name fields name
        })

        client.sync.execute {
            select in INDEX -> TYPE searchType QueryAndFetch from (search.pageNumber - 1) * limit size limit sort2 {
                _sort(search)
            } query2 {
                query
            } facets {
                facets
            }
        }
    }

    def _sort(search: SavedSearch) = search.sortType match {

        case Sort.Random =>
            SortBuilders
              .scriptSort("Math.random()", "number")
              .order(SortOrder.ASC)

        case Sort.Attribute if search.sortAttribute != null =>
            SortBuilders
              .fieldSort(FIELD_ATTRIBUTE + search.sortAttribute.id)
              .order(SortOrder.ASC)
              .ignoreUnmapped(true)

        case Sort.AttributeDesc if search.sortAttribute != null =>
            SortBuilders
              .fieldSort(FIELD_ATTRIBUTE + search.sortAttribute.id)
              .order(SortOrder.DESC)
              .ignoreUnmapped(true)

        case Sort.Name => SortBuilders.fieldSort(FIELD_NAME_NOT_ANALYSED).order(SortOrder.ASC)
        case Sort.Oldest => SortBuilders.fieldSort("objectid").order(SortOrder.ASC)
        case _ => SortBuilders.fieldSort("objectid").order(SortOrder.DESC)
    }

    def _resp2facets(resp: SearchResponse, attributes: Iterable[Attribute]): Seq[com.cloudray.scalapress.search.Facet] = {
        Option(resp.getFacets) match {
            case None => Nil
            case Some(facets) =>
                facets.facets().asScala
                  .filter(_.isInstanceOf[TermsFacet])
                  .map(_.asInstanceOf[TermsFacet])
                  .map(facet => {
                    val terms = facet
                      .getEntries
                      .asScala
                      .map(entry => FacetTerm(_attributeRestore(entry.getTerm.string()), entry.getCount))
                      .toSeq
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
            val objectType = arg.getSource.get("objectType").toString.toLong
            val n = arg.getSource.get(FIELD_NAME_NOT_ANALYSED).toString
            val status = arg.getSource.get(FIELD_STATUS).toString
            val attributes = arg.getSource.asScala
              .filter(_._2 != null)
              .filter(_._1.startsWith(FIELD_ATTRIBUTE)).map(field => {
                val id = field._1.drop(FIELD_ATTRIBUTE.length).toLong
                val value = _attributeRestore(field._2.toString)
                (id, value)
            }).toMap
            new ObjectRef(id, objectType, n, status, attributes, Nil)
        }).toSeq
    }

    def _source(obj: Obj) = {
        require(obj.id > 0)

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("objectid", obj.id)
          .field("objectType", obj.objectType.id.toString)
          .field(FIELD_NAME, _normalize(obj.name))
          .field(FIELD_NAME_NOT_ANALYSED, obj.name)
          .field(FIELD_STATUS, obj.status)

        Option(obj.labels).foreach(tags => tags.split(",").foreach(tag => json.field(FIELD_TAGS, tag)))

        val hasImage = obj.images.size > 0
        json.field("hasImage", hasImage.toString)

        val folderIds = obj.folders.asScala.map(_.id.toString)
        json.field(FIELD_FOLDERS, folderIds.toSeq: _*)

        obj.attributeValues.asScala
          .filterNot(_.value == null)
          .filterNot(_.value.isEmpty)
          .foreach(av => {
            json.field(FIELD_ATTRIBUTE + av.attribute.id.toString, _attributeNormalize(av.value))
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
            map.put("jvm.mem.nonHeapCommitted", node.getJvm.mem.nonHeapCommitted.mb + "mb")
            map.put("jvm.mem.nonHeapUsed", node.getJvm.mem.nonHeapUsed.mb + "mb")
            map.put("jvm.threads.count", node.getJvm.threads.count.toString)
            map.put("jvm.threads.peakCount", node.getJvm.threads.peakCount.toString)

            map.put("indices.docs.count", node.getIndices.getDocs.getCount.toString)
            map.put("indices.docs.deleted", node.getIndices.getDocs.getDeleted.toString)

            map.put("indices.store.size", node.getIndices.getStore.size.mb + "mb")
            map.put("indices.store.throttleTime", node.getIndices.getStore.throttleTime.toString)

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


