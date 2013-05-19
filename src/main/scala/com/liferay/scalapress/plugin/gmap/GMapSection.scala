package com.liferay.scalapress.plugin.gmap

import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{Entity, Table}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.obj.attr.AttributeFuncs
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_mapping_section")
class GMapSection extends Section {

    @BeanProperty var postcode: String = _

    override def backoffice: String = "/backoffice/plugin/mapping/section/" + id

    def render(request: ScalapressRequest): Option[String] = {

        Option(postcode).filter(_.trim.length > 0)
          .orElse(request.obj.flatMap(obj => AttributeFuncs.attributeValue(obj, "postcode")))
          .map(arg => {

            val pc = arg.replace(" ", "")

            val iframeSrc = "https://maps.google.co.uk/maps?q=" + pc + "&z=10&output=embed"
            val hrefSrc = "https://maps.google.co.uk/maps?q=" + pc + "&z=10"
            val sectionId = "section-" + id
            val html =
                <div class="gmap-section" id={sectionId}>
                    <iframe width="100%" height="400" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src={iframeSrc}></iframe>
                    <br/>
                    <small>
                        <a href={hrefSrc} style="color:#0000FF;text-align:left">View Larger Map</a>
                    </small>
                </div>

            html.toString()
        })
    }

    def desc: String = "Google maps embedded iframe"
}
