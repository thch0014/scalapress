package com.liferay.scalapress.plugin.mapping

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest, Section}
import javax.persistence.{Entity, Table}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_mapping_section")
class GMapSection extends Section {

    @BeanProperty var postcode: String = _

    override def backoffice: String = "/backoffice/plugin/mapping/section/" + id

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(postcode).map(_.replace(" ", "")).map(arg => {
            val iframeSrc = "https://maps.google.co.uk/maps?q=" + postcode + "&z=14&output=embed"
            val hrefSrc = "https://maps.google.co.uk/maps?q=" + postcode + "&z=14"
            val html =
                <div class="gmap-section">
                    <iframe width="425" height="350" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src={iframeSrc}></iframe>
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
