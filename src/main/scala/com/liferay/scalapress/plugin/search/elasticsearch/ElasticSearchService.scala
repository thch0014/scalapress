package com.liferay.scalapress.plugin.search.elasticsearch

import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.action.search.{SearchType, SearchResponse}
import com.liferay.scalapress.{ScalapressContext, Logging}
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
import com.liferay.scalapress.enums.{AttributeType, Sort}
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import com.liferay.scalapress.obj.{ObjectType, ObjectDao, Obj}
import com.liferay.scalapress.folder.FolderDao
import com.liferay.scalapress.util.geo.Postcode
import org.elasticsearch.common.unit.DistanceUnit
import javax.annotation.PreDestroy
import java.util.concurrent.TimeUnit
import com.liferay.scalapress.search.{ObjectRef, SavedSearch, SearchService}
import scala.Some

/** @author Stephen Samuel */

@Component
class ElasticSearchService extends SearchService with Logging {

    val FIELD_ATTRIBUTE = "attribute_"
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
          .startObject("_id")
          .field("type", "string")
          .field("index", "not_analyzed")
          .field("store", "yes")
          .endObject()
          .startObject("objectid")
          .field("type", "integer")
          .field("index", "not_analyzed")
          .field("store", "yes")
          .endObject()
          .startObject("name_raw")
          .field("type", "string")
          .field("index", "not_analyzed")
          .field("store", "yes")
          .endObject()
          .startObject("location")
          .field("type", "geo_point")
          .field("index", "not_analyzed")
          .endObject()

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
        objs.filter(_.name != null).filter(!_.name.isEmpty).foreach(index(_))

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
        resp.hits.totalHits() match {
            case 1 => true
            case _ => false
        }
    }

    override def index(obj: Obj) {
        logger.debug("Indexing [{}, {}]", obj.id, obj.name)
        val src = source(obj)

        try {

            //          client.prepareDelete(INDEX, obj.objectType.id.toString, obj.id.toString).execute().actionGet(TIMEOUT)
            client.prepareIndex(INDEX, TYPE, obj.id.toString)
              .setSource(src)
              .execute()
              .get(TIMEOUT, TimeUnit.MILLISECONDS)

        } catch {
            case e: Exception => logger.warn(e.getMessage)
        }
    }

    override def typeahead(q: String, limit: Int): Seq[ObjectRef] = {
        val resp = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setQuery(new PrefixQueryBuilder("name", q.toLowerCase))
          .setFrom(0)
          .setSize(limit)
          .execute()
          .actionGet()
        _resp2ref(resp)
    }

    override def count(search: SavedSearch): Long = {
        val query = _buildQuery(search)
        client.prepareCount(INDEX)
          .setQuery(query)
          .execute()
          .actionGet(4000)
          .count()
    }

    override def search(search: SavedSearch): Seq[ObjectRef] = {
        val resp = _search(search)
        val refs = _resp2ref(resp)
        logger.debug("Search returned {} refs", refs.size)
        refs
    }

    def _buildQuery(search: SavedSearch): QueryStringQueryBuilder = {
        val queryString = _buildQueryString(search)
        queryString.length match {
            case 0 => new QueryStringQueryBuilder("*:*")
            case _ => new QueryStringQueryBuilder(queryString).defaultOperator(QueryStringQueryBuilder.Operator.AND)
        }
    }

    def _buildQueryString(search: SavedSearch): String = {

        val buffer = new ArrayBuffer[String]()

        Option(search.labels)
          .filter(_.trim.length > 0)
          .filterNot(_.toLowerCase == "random")
          .filterNot(_.toLowerCase == "latest")
          .foreach(_.split(",").foreach(label => buffer.append("labels:" + label)))

        if (search.name == null)
            search.name = search.keywords

        Option(search.name)
          .filter(_.trim.length > 0)
          .foreach(name => name
          .trim
          .split(" ")
          .filter(_.trim.length > 0)
          .map(_.replace("!", "").toLowerCase)
          .foreach(arg => buffer.append(s"name:${arg}")))

        Option(search.objectType).foreach(arg => buffer.append("objectType:" + arg.id.toString))

        //            Option(search.keywords)
        //          .filter(_.trim.length > 0)
        //      .foreach(_.split(",").foreach(c => buffer.append("content:" + c)))

        Option(search.searchFolders)
          .filter(_.trim.length > 0)
          .map(_.replaceAll("\\D", ""))
          .foreach(_.split(",").foreach(f => buffer.append("folders:" + f)))

        search.attributeValues.asScala.filter(_.value.trim.length > 0).foreach(av => {
            buffer.append(FIELD_ATTRIBUTE + av.attribute.id + ":" + av.value.replace(" ", "_"))
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
            new GeoDistanceFilterBuilder("location")
              .point(gps.lat, gps.lon)
              .distance(search.distance, DistanceUnit.MILES)
        })

        val limit = if (search.maxResults < 1) 40 else search.maxResults

        val req = client.prepareSearch(INDEX)
          .setSearchType(SearchType.QUERY_AND_FETCH)
          .setTypes(TYPE)
          .setFrom(0)
          .setSize(limit)

        val query = _buildQuery(search)

        filter match {
            case None => req.setQuery(query)
            case Some(f) => req.setQuery(new FilteredQueryBuilder(query, f))
        }

        val sort = search.sortType match {
            case Sort.Random => SortBuilders
              .scriptSort("Math.random()", "number")
              .order(SortOrder.ASC)
            case Sort.Attribute if search.sortAttribute != null =>
                SortBuilders.fieldSort("attribute_" + search.sortAttribute.id).order(SortOrder.ASC).ignoreUnmapped(true)
            case Sort.AttributeDesc if search.sortAttribute != null =>
                SortBuilders
                  .fieldSort(FIELD_ATTRIBUTE + search.sortAttribute.id)
                  .order(SortOrder.DESC)
                  .ignoreUnmapped(true)
            case Sort.Name => SortBuilders.fieldSort("name_raw").order(SortOrder.ASC)
            case Sort.Oldest => SortBuilders.fieldSort("objectid").order(SortOrder.ASC)
            case _ => SortBuilders.fieldSort("objectid").order(SortOrder.DESC)
        }

        req.addSort(sort)
        logger.debug("Search: " + req)
        req.execute().actionGet()
    }

    def _resp2ref(resp: SearchResponse): Seq[ObjectRef] = {
        resp.hits().asScala.map(arg => {
            val id = arg.id.toLong
            val objectType = arg.getSource.get("objectType").toString.toLong
            val name = arg.getSource.get("name_raw").toString
            val status = arg.getSource.get("status").toString
            val attributes = arg.getSource.asScala
              .filter(_._2 != null)
              .filter(_._1.startsWith(FIELD_ATTRIBUTE)).map(field => {
                val id = field._1.drop("attribute_".length).toLong
                val value = field._2.toString
                (id, value)
            }).toMap
            new ObjectRef(id, objectType, name, status, attributes, Nil)
        }).toSeq
    }

    // search by the given query string and then return the matching doc ids
    def search(q: String, pageSize: Int): SearchResponse = {
        val s = new SavedSearch
        s.name = q
        s.maxResults = pageSize
        _search(s)
    }

    // search by the given query string and then return the matching doc ids
    override def searchType(q: String, t: ObjectType, pageSize: Int): SearchResponse = {
        val s = new SavedSearch
        s.name = q
        s.objectType = t
        s.maxResults = pageSize
        _search(s)
    }

    private def source(obj: Obj) = {
        require(obj.id > 0)

        val json = XContentFactory
          .jsonBuilder()
          .startObject()
          .field("objectid", obj.id)
          .field("objectType", obj.objectType.id.toString)
          .field("name", obj.name.toLowerCase)
          .field("name_raw", obj.name)
          .field("status", obj.status)
          .field("labels", obj.labels)

        val hasImage = obj.images.size > 0
        json.field("hasImage", hasImage.toString)

        val folderIds = obj.folders.asScala.map(_.id.toString)
        json.field("folders", folderIds.toSeq: _*)

        obj.attributeValues.asScala.foreach(av => {
            json.field(FIELD_ATTRIBUTE + av.attribute.id.toString, av.value.replace(" ", "_"))
            json.field("has_attribute_" + av.attribute.id.toString, "1")
            if (av.attribute.attributeType == AttributeType.Postcode) {
                logger.debug("postcode=" + av.value)
                Postcode.gps(av.value).foreach(gps => {
                    json.field("location", gps.string())
                    logger.debug("location=" + gps.string())
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
}

