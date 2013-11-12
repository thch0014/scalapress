package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.AttributeType
import com.cloudray.scalapress.util.geo.Postcode
import com.sksamuel.elastic4s.ElasticDsl._
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.framework.Logging
import scala.collection.JavaConverters._
import com.sksamuel.elastic4s.BulkCompatibleDefinition

/** @author Stephen Samuel */
object ElasticsearchIndexBuilder extends ElasticsearchUtils with Logging {

  def build(items: Seq[Item]): Seq[BulkCompatibleDefinition] = {

    items.filter(_.itemType != null).map(item => {
      Option(item.status).getOrElse(Item.STATUS_DELETED).toLowerCase match {

        case DELETED | DISABLED =>
          logger.debug("Creating delete operation [id={}]", item.id)
          new DeleteByIdDefinition(INDEX, TYPE, item.id.toString)

        case LIVE =>
          val _fields = ListBuffer[(String, Any)](
            FIELD_ITEM_ID -> item.id,
            FIELD_ITEM_TYPE_ID -> item.itemType.id.toString,
            FIELD_NAME -> normalize(item.name),
            FIELD_NAME_NOT_ANALYSED -> item.name,
            FIELD_STATUS -> item.status,
            FIELD_PRIORITIZED -> (if (item.prioritized) 1 else 0)
          )

          Option(item.labels).foreach(tags => tags.split(",").foreach(tag => _fields.append(FIELD_TAGS -> tag)))

          //          Option(item.labels)
          //            .foreach(tags => tags
          //            .split(",")
          //            .foreach(tag => json.field(FIELD_TAGS, tag)))

          val hasImage = item.images.size > 0
          _fields.append(FIELD_HAS_IMAGE -> hasImage.toString)

          item.folders.asScala.foreach(folder => _fields.append(FIELD_FOLDERS -> folder.id))

          item.attributeValues.asScala
            .filterNot(_.value == null)
            .filterNot(_.value.isEmpty)
            .foreach(av => {
            _fields.append(attributeField(av.attribute) -> attributeEscape(av.value))
            _fields.append("has_attribute_" + av.attribute.id.toString -> "1")
            if (av.attribute.attributeType == AttributeType.Postcode) {
              Postcode.gps(av.value).foreach(gps => {
                _fields.append(FIELD_LOCATION -> gps.string())
              })
            }
          })

          insert into INDEX -> TYPE id item.id fields _fields
      }
    })
  }
}
