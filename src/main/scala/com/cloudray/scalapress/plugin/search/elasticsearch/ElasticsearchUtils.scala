package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.item.attr.Attribute
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.search.Search

/** @author Stephen Samuel */
trait ElasticsearchUtils {

  val MAX_RESULTS_HARD_LIMIT = 1000
  val DEFAULT_MAX_RESULTS = 200

  val TIMEOUT = 5000
  val INDEX = "scalapress"
  val TYPE = "item"

  val DELETED = Item.STATUS_DELETED.toLowerCase
  val DISABLED = Item.STATUS_DISABLED.toLowerCase
  val LIVE = Item.STATUS_LIVE.toLowerCase

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
  val FIELD_ATTRIBUTE = "attribute_"
  val FIELD_PRICE = "price"

  def attributeField(attribute: Attribute): String = attributeField(attribute.id)
  def attributeField(id: Any): String = FIELD_ATTRIBUTE + id

  def attributeEscape(value: String): String = value.replace("!", "").replace(" ", "_").replace("/", "_")
  def normalize(value: String): String = value.replace("!", "").replace("/", "_").toLowerCase
  def attributeUnescape(value: String): String = value.replace("_", " ")

  def _maxResults(search: Search) = {
    if (search.page.pageSize < 1) DEFAULT_MAX_RESULTS
    else if (search.page.pageSize > MAX_RESULTS_HARD_LIMIT) MAX_RESULTS_HARD_LIMIT
    else search.page.pageSize
  }
}
