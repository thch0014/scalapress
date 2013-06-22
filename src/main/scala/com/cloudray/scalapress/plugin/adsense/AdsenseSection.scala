package com.cloudray.scalapress.plugin.adsense

import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.ScalapressRequest
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_adsense_section")
class AdsenseSection extends Section {

    @BeanProperty var googleAdClient: String = _
    @BeanProperty var googleAdSlot: String = _
    @BeanProperty var googleAdWidth: String = _
    @BeanProperty var googleAdHeight: String = _

    def desc: String = "Google adsense block"
    override def backoffice: String = "/backoffice/plugin/adsense/section/" + id

    def render(request: ScalapressRequest): Option[String] = {
        try {
            Option(googleAdClient).filterNot(_.isEmpty).map(arg => {
                engine
                  .layout("/com/cloudray/scalapress/plugin/adsense/adsense.ssp",
                    Map("client" -> googleAdClient,
                        "slot" -> googleAdSlot,
                        "width" -> googleAdWidth.toInt,
                        "height" -> googleAdHeight.toInt))
            })
        } catch {
            case e: Exception => None
        }
    }
}