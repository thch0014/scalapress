package com.cloudray.scalapress.plugin.search.elasticsearch

import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.SearchType
import com.cloudray.scalapress.{ScalapressContext, Logging}
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.{Value, Autowired}
import com.googlecode.genericdao.search.Search
import org.elasticsearch.common.settings.ImmutableSettings
import java.io.File
import java.util.UUID
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.index.query._
import collection.mutable.ArrayBuffer
import scala.Option
import com.cloudray.scalapress.enums.{AttributeType, Sort}
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.util.geo.Postcode
import org.elasticsearch.common.unit.DistanceUnit
import javax.annotation.PreDestroy
import java.util.concurrent.TimeUnit
import com.cloudray.scalapress.search._
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.facet.terms.{TermsFacet, TermsFacetBuilder}
import scala.Some
import com.cloudray.scalapress.search.ObjectRef
import com.cloudray.scalapress.search.SearchResult

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

    @Value("${search.index.preload.size}") var preloadSize: Int = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _
    var setup = false

    val tempDir = File.createTempFile("anything", "tmp").getParent
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

    val node = NodeBuilder.nodeBuilder().local(true).data(true).settings(settings).node()
    val client = node.client()

    def setupIndex() {

        val source = XContentFactory
          .jsonBuilder()
          .startObject()
          .startObject("mappings")
          .startObject(TYPE)
          //   .startObject("_source").field("enabled", false).endObject()
          .startObject("properties")
          .startObject("_id").field("type", "string").field("index", "not_analyzed").field("store", "yes").endObject()
          .startObject("objectid").field("type", "integer").field("index", "not_analyzed").field("store", "yes").endObject()
          .startObject(FIELD_NAME_NOT_ANALYSED).field("type", "string").field("index", "not_analyzed").field("store", "yes").endObject()
          .startObject(FIELD_LOCATION).field("type", "geo_point").field("index", "not_analyzed").endObject()
          .startObject(FIELD_TAGS).field("type", "string").field("index", "not_analyzed").endObject()

        context.attributeDao.findAll().foreach(attr => {
            source
              .startObject("attribute_" + attr.id)
              .field("type", "string")
              .field("index", "not_analyzed")
              .endObject()
        })

        source.endObject()
          .endObject()
          .endObject()
          .endObject()

        client.admin().indices().prepareCreate(INDEX).setSource(source).execute().actionGet(TIMEOUT)
    }

    @Transactional
    def index() {

        if (!setup) {
            setupIndex()
            setup = true
        }

        val objs = objectDao.search(new Search(classOf[Obj])
          .addFilterLike("status", "live")
          .addFilterNotEqual("objectType.name", "Account")
          .addFilterNotEqual("objectType.name", "account")
          .addFilterNotEqual("objectType.name", "Accounts")
          .addFilterNotEqual("objectType.name", "accounts")
          .setMaxResults(preloadSize))

        logger.info("Indexing {} objects", objs.size)
        objs.filterNot(_.name == null).filterNot(_.name.isEmpty).foreach(index(_))

        logger.info("Indexing finished")
    }

    override def contains(id: String): Boolean = {
        val resp = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setTypes(TYPE)
          .setQuery(new TermQueryBuilder("_id", id))
          .setFrom(0)
          .setSize(1)
          .execute()
          .actionGet()
        resp.getHits.totalHits() match {
            case 1 => true
            case _ => false
        }
    }

    def _normalize(value: String) = value.replace("!", "").toLowerCase

    override def index(obj: Obj) {
        logger.debug("Indexing [{}, {}]", obj.id, obj.name)
        val src = source(obj)

        try {

            // client.prepareDelete(INDEX, obj.objectType.id.toString, obj.id.toString).execute().actionGet(TIMEOUT)
            client.prepareIndex(INDEX, TYPE, obj.id.toString)
              .setSource(src)
              .execute()
              .actionGet(TIMEOUT, TimeUnit.MILLISECONDS)

        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }
    }

    override def typeahead(q: String, limit: Int): Seq[ObjectRef] = {
        val resp = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setQuery(new PrefixQueryBuilder(FIELD_NAME, q.toLowerCase))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
        _resp2ref(resp)
    }

    override def count: Long = {
        client.prepareCount(INDEX).setQuery(new QueryStringQueryBuilder("*:*")).execute().actionGet(4000).getCount
    }

    def _count(search: SavedSearch): Int = {
        val query = _buildQuery(search)
        client.prepareCount(INDEX)
          .setQuery(query)
          .execute()
          .actionGet(4000)
          .getCount.toInt
    }

    override def search(search: SavedSearch): SearchResult = {
        val resp = _search(search)
        val refs = _resp2ref(resp)
        val facets = _resp2facets(resp)
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
            buffer.append(FIELD_ATTRIBUTE + av.attribute.id + ":" + _normalize(av.value.replace(" ", "_")))
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

        val limit = _maxResults(search)
        val query = _buildQuery(search)
        val sort = _sort(search)

        val req = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setTypes(TYPE)
          .setFrom(0)
          .setSize(limit)
          .addSort(sort)

        filter match {
            case None => req.setQuery(query)
            case Some(f) => req.setQuery(new FilteredQueryBuilder(query, f))
        }

        search.facets.map(facet => {
            req.addFacet(new TermsFacetBuilder(facet).field(facet))
        })

        logger.debug("Search: " + req)
        req.execute().actionGet()
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

    def _resp2facets(resp: SearchResponse): Seq[Facet] = {
        Option(resp.getFacets) match {
            case None => Nil
            case Some(facets) =>
                facets.facets()
                  .asScala
                  .filter(_.isInstanceOf[TermsFacet])
                  .map(_.asInstanceOf[TermsFacet])
                  .map(arg => {
                    val terms = arg.getEntries.asScala.map(entry => FacetTerm(entry.getTerm, entry.getCount))
                    Facet(arg.getName, terms)
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
                val value = field._2.toString
                (id, value)
            }).toMap
            new ObjectRef(id, objectType, n, status, attributes, Nil)
        }).toSeq
    }

    private def source(obj: Obj) = {
        require(obj.id > 0)

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("objectid", obj.id)
          .field("objectType", obj.objectType.id.toString)
          .field(FIELD_NAME, _normalize(obj.name))
          .field(FIELD_NAME_NOT_ANALYSED, obj.name)
          .field(FIELD_STATUS, obj.status)

        logger.trace("Adding tags [{}]", obj.labels)
        Option(obj.labels).foreach(tags => tags.split(",").foreach(tag => json.field(FIELD_TAGS, tag)))

        val hasImage = obj.images.size > 0
        json.field("hasImage", hasImage.toString)

        val folderIds = obj.folders.asScala.map(_.id.toString)
        logger.trace("Adding folders [{}]", folderIds)
        json.field(FIELD_FOLDERS, folderIds.toSeq: _*)

        logger.trace("Processing attributes")
        obj.attributeValues.asScala
          .filterNot(_.value == null)
          .filterNot(_.value.isEmpty)
          .foreach(av => {
            json.field(FIELD_ATTRIBUTE + av.attribute.id.toString, _normalize(av.value.replace(" ", "_")))
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
        node.close()
        dataDir.delete()
    }

    def stats: Map[String, String] = {
        val nodes = client.admin().cluster().prepareNodesStats().all().execute().actionGet(TIMEOUT).getNodes
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


