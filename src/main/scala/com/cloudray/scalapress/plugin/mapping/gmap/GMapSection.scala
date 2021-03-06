package com.cloudray.scalapress.plugin.mapping.gmap

import javax.persistence.{Entity, Table}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.item.attr.AttributeFuncs
import scala.beans.BeanProperty
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_mapping_section")
class GMapSection extends Section {

  @BeanProperty
  var postcode: String = _

  override def render(request: ScalapressRequest): Option[String] = {

    Option(postcode)
      .filter(_.trim.length > 0)
      .orElse(request.item.flatMap(obj => AttributeFuncs.attributeValue(obj, "postcode")))
      .map(pc => {
      val sectionId = "section-" + id
      val html =
        <div class="gmap-section" id={sectionId}>
          <iframe width="100%" height="400" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src={_iframe(
            pc)}></iframe>
          <br/>
          <small>
            <a href={_href(pc)} style="color:#0000FF;text-align:left">View Larger Map</a>
          </small>
        </div>

      html.toString()
    })
  }

  def _iframe(pc: String) = "https://maps.google.co.uk/maps?q=" + pc.replace(" ", "") + "&z=10&output=embed"
  def _href(pc: String) = "https://maps.google.co.uk/maps?q=" + pc.replace(" ", "") + "&z=10"

  override def desc: String = "Google maps embedded iframe"
  override def backoffice: String = "/backoffice/plugin/mapping/section/" + id
}
