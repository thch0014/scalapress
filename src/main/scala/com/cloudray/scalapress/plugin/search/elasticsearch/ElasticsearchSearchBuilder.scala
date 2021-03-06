package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.search.{TagsFacetField, Sort, AttributeFacetField, Search}
import com.sksamuel.elastic4s.FacetDefinition
import com.sksamuel.elastic4s.ElasticDsl._
import com.cloudray.scalapress.util.geo.Postcode
import org.elasticsearch.common.unit.DistanceUnit
import org.elasticsearch.search.sort.SortOrder
import scala.Some
import com.sksamuel.elastic4s.SearchType.QueryAndFetch

/**
 * Builder for Elasticsearch Queries from a Search object
 * @author Stephen Samuel */
object ElasticsearchSearchBuilder extends ElasticsearchUtils {

  def build(search: Search): SearchDefinition = {

    val filter = search.location.flatMap(Postcode.gps).map(gps => {
      geoDistance(FIELD_LOCATION).point(gps.lat, gps.lon).distance(search.distance, DistanceUnit.MILES)
    })

    val q = filter match {
      case None => ElasticsearchQueryBuilder.build(search)
      case Some(f) => filteredQuery.query(ElasticsearchQueryBuilder.build(search)).filter(f)
    }

    val _offset = search.page.offset
    val _limit = _maxResults(search)
    val _priority = by field FIELD_PRIORITIZED order SortOrder.DESC
    val _sort = sort(search)
    val sorts = if (search.ignorePriority) List(_sort) else List(_priority, _sort)

    select in (INDEX -> TYPE) searchType QueryAndFetch from _offset size _limit sort (sorts: _*) query q facets {
      facets(search)
    }
  }

  def facets(search: Search): Iterable[FacetDefinition] = {
    search.facets.map {
      case AttributeFacetField(id) => facet terms id.toString fields attributeField(id) size 10
      case TagsFacetField => facet terms "tags" size 10
    }
  }

  def sort(search: Search) = search.sort match {
    case Sort.Random => by script "Math.random()" as "number" order SortOrder.ASC
    case Sort.Attribute if search.sortAttribute.isDefined =>
      by field attributeField(search.sortAttribute.get) order SortOrder.ASC missing "_last"
    case Sort.AttributeDesc if search.sortAttribute.isDefined =>
      by field attributeField(search.sortAttribute.get) order SortOrder.DESC missing "_last"
    case Sort.Name => by field FIELD_NAME_NOT_ANALYSED order SortOrder.ASC
    case Sort.Price => by field FIELD_PRICE order SortOrder.ASC
    case Sort.PriceHigh => by field FIELD_PRICE order SortOrder.DESC
    case Sort.Oldest => by field FIELD_ITEM_ID order SortOrder.ASC
    case _ => by field FIELD_ITEM_ID order SortOrder.DESC
  }

}
