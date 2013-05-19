package com.liferay.scalapress.plugin.bingmaps

import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{Entity, Table}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.obj.attr.AttributeFuncs
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_bingmap_section")
class BingMapSection extends Section {

    @BeanProperty var postcode: String = _

    override def backoffice: String = "/backoffice/plugin/bingmap/section/" + id

    def render(request: ScalapressRequest): Option[String] = {

        Option(postcode)
          .filter(_.trim.length > 0)
          .orElse(request.obj.flatMap(obj => AttributeFuncs.attributeValue(obj, "postcode")))
          .map(arg => {

            val pc = arg.replaceAll("\\s", "")

            val iframeSrc = "http://www.bing.com/maps/?v=2&cp=l=16&where1=" + pc
            val hrefSrc = "http://www.bing.com/maps/?v=2&cp=l=16&where1=" + pc
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

    def desc: String = "Bing maps embedded iframe"
}
