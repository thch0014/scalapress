package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.search.Search
import com.sksamuel.elastic4s.{FilterDefinition, QueryDefinition}
import scala.collection.mutable.ListBuffer
import com.sksamuel.elastic4s.ElasticDsl._

/** @author Stephen Samuel */
object ElasticsearchQueryBuilder extends ElasticsearchUtils {

  def build(search: Search): QueryDefinition = {

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
      .map(value => normalize(value))
      .foreach(value => queries.append(field("name", value)))
    )

    search.attributeValues
      .filterNot(_.value.trim.toLowerCase == "any")
      .map(av => termFilter(attributeField(av.id), attributeEscape(av.value)))
      .foreach(filters append _)

    search.hasAttributes.foreach(arg => filters.append(termFilter("has_attribute_" + arg, "1")))

    search.tags
      .filterNot(_.isEmpty)
      .filterNot(_.toLowerCase == "random")
      .filterNot(_.toLowerCase == "latest")
      .foreach(tag => filters.append(termFilter(FIELD_TAGS, tag)))

    search.ignorePast
      .foreach(attr => filters.append(numericRangeFilter(attributeField(attr)).gte(System.currentTimeMillis())))

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
}
