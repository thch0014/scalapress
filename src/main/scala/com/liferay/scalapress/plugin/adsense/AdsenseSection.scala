package com.liferay.scalapress.plugin.adsense

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.section.Section
import scala.reflect.BeanProperty
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_adsense_section")
class AdsenseSection extends Section {

    @BeanProperty var googleAdClient: String = _
    @BeanProperty var googleAdSlot: String = _
    @BeanProperty var googleAdWidth: String = _
    @BeanProperty var googleAdHeight: String = _

    override def backoffice: String = "/backoffice/plugin/adsense/section/" + id

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        try {
            Option(googleAdClient).filter(_.trim.length > 0).map(arg => {
                googleAdWidth.toInt
                googleAdHeight.toInt
                val rendered = String.format(AdsenseSection.TEMPLATE,
                    googleAdClient,
                    googleAdSlot,
                    googleAdWidth,
                    googleAdHeight)
                rendered
            })
        } catch {
            case e: Exception => None
        }
    }

    def desc: String = "Google adsense block"
}

object AdsenseSection {
    val TEMPLATE = """<script type="text/javascript">
                        google_ad_client = "%s";
                        google_ad_slot = "%s";
                        google_ad_width = %s;
                        google_ad_height = %s;
                     </script>
                     <script type="text/javascript" src="http://pagead2.googlesyndication.com/pagead/show_ads.js"></script>"""
}