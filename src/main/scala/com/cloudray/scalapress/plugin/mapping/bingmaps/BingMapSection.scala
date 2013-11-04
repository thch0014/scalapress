package com.cloudray.scalapress.plugin.mapping.bingmaps

import javax.persistence.{Entity, Table}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.item.attr.AttributeFuncs
import scala.beans.BeanProperty
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_bingmap_section")
class BingMapSection extends Section {

  @BeanProperty
  var postcode: String = _

  override def backoffice: String = "/backoffice/plugin/bingmap/section/" + id
  override def desc: String = "Bing maps embedded iframe"

  override def render(request: ScalapressRequest): Option[String] = {

    Option(postcode)
      .filter(_.trim.length > 0)
      .orElse(request.item.flatMap(obj => AttributeFuncs.attributeValue(obj, "postcode")))
      .map(arg => {

      val pc = arg.replaceAll("\\s", "")
      val iframeSrc = "http://www.bing.com/maps/?v=2&cp=l=16&where1=" + pc
      val hrefSrc = "http://www.bing.com/maps/?v=2&cp=l=16&where1=" + pc
      val sectionId = "section-" + id

      Scalate.layout("/com/cloudray/scalapress/plugin/bingmaps/section.ssp",
        Map("iframeSrc" -> iframeSrc, "hrefSrc" -> hrefSrc, "sectionId" -> sectionId))
    })
  }
}
