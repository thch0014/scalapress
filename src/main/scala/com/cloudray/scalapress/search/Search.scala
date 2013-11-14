package com.cloudray.scalapress.search

import com.cloudray.scalapress.item.attr.{AttributeSelection, Attribute}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.util.Page

/** @author Stephen Samuel */
case class Search(status: Option[String] = None,
                  name: Option[String] = None,
                  prefix: Option[String] = None,
                  itemTypeId: Option[Long] = None,
                  folders: Iterable[String] = Nil,
                  attributeValues: Iterable[AttributeSelection] = Nil,
                  hasAttributes: Iterable[String] = Nil,
                  imagesOnly: Boolean = false,
                  tags: Iterable[String] = Nil,
                  location: Option[String] = None,
                  distance: Int = 100,
                  inStockOnly: Boolean = false,
                  minPrice: Int = 0,
                  maxPrice: Int = 0,
                  ignorePast: Option[Attribute] = None,
                  newerThanTimestamp: Long = 0,
                  ignorePriority: Boolean = false,
                  facets: Iterable[FacetField] = Nil, // the facets to return
                  sort: Sort = Sort.Name,
                  sortAttribute: Option[Attribute] = None,
                  page: Page = Page.empty)

object Search {

  val DEFAULT_MAX_RESULTS = 100

  def empty: Search = new Search

  def apply(name: String): Search = Search(name = Option(name))

  def apply(saved: SavedSearch): Search = {

    val folders = Option(saved.searchFolders).map(_.trim).filterNot(_.isEmpty) match {
      case None => Nil
      case Some(ids) => ids.split(",").map(_.trim).toSeq
    }

    val hasAttributes = Option(saved.hasAttributes).map(_.trim).filterNot(_.isEmpty) match {
      case None => Nil
      case Some(ids) => ids.split(",").map(_.trim).toSeq
    }

    val tags = Option(saved.labels).map(_.trim).filterNot(_.isEmpty) match {
      case None => Nil
      case Some(ids) => ids.split(",").map(_.trim).toSeq
    }

    val pageSize = if (saved.maxResults <= 0) DEFAULT_MAX_RESULTS else saved.maxResults

    new Search(status = Option(saved.status),
      name = Option(saved.name).map(_.trim).filterNot(_.isEmpty),
      prefix = Option(saved.prefix).filterNot(_.trim.isEmpty),
      itemTypeId = Option(saved.itemType).map(_.id),
      folders = folders,
      attributeValues = saved.attributeValues.asScala.map(av => AttributeSelection(av.id.toString, av.value)),
      hasAttributes = hasAttributes,
      imagesOnly = saved.imageOnly,
      tags = tags,
      location = Option(saved.location).map(_.trim).filterNot(_.isEmpty),
      distance = saved.distance,
      inStockOnly = saved.inStockOnly,
      minPrice = saved.minPrice,
      maxPrice = saved.maxPrice,
      ignorePast = Option(saved.ignorePast),
      newerThanTimestamp = saved.newerThanTimestamp,
      facets = Nil,
      sort = Option(saved.sortType).getOrElse(Sort.Name),
      sortAttribute = Option(saved.sortAttribute),
      page = Page(1, pageSize)
    )
  }
}