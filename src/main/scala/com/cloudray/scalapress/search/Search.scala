package com.cloudray.scalapress.search

import com.cloudray.scalapress.item.ItemType
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search

/** @author Stephen Samuel */
case class Search(status: Option[String] = None,
                  name: Option[String] = None,
                  prefix: Option[String] = None,
                  itemType: Option[ItemType] = None,
                  folders: Iterable[Any] = Nil,
                  attributeValues: Iterable[AttributeValue] = Nil,
                  hasAttributes: Iterable[Any] = Nil,
                  imagesOnly: Boolean = false,
                  tags: Iterable[String] = Nil,
                  location: Option[String] = None,
                  distance: Int = 100,
                  inStockOnly: Boolean = false,
                  minPrice: Int = 0,
                  maxPrice: Int = 0,
                  ignorePast: Option[Attribute] = None,
                  newerThanTimestamp: Long = 0,
                  facets: Iterable[FacetField] = Nil,
                  selectedFacets: Iterable[SelectedFacet] = Nil,
                  sort: Sort = Sort.Name,
                  sortAttribute: Option[Attribute] = None,
                  pageNumber: Int = 1,
                  maxResults: Int = Search.DEFAULT_MAX_RESULTS)

object Search {

  val DEFAULT_MAX_RESULTS = 100

  def apply(name: String): Search = Search(name = Option(name))

  def apply(saved: SavedSearch): Search = {
    search.Search(Option(saved.status),
      Option(saved.name),
      Option(saved.prefix),
      Option(saved.objectType),
      Option(saved.searchFolders).getOrElse("").split(","),
      saved.attributeValues.asScala,
      Option(saved.hasAttributes).getOrElse("").split(","),
      saved.imageOnly,
      Option(saved.labels).getOrElse("").split(","),
      Option(saved.location),
      saved.distance,
      saved.inStockOnly,
      saved.minPrice,
      saved.maxPrice,
      Option(saved.ignorePast),
      saved.newerThanTimestamp,
      Nil,
      Nil,
      Option(saved.sortType).getOrElse(Sort.Name),
      Option(saved.sortAttribute),
      pageNumber = 1,
      if (saved.maxResults == 0) DEFAULT_MAX_RESULTS else saved.maxResults
    )
  }
}