package com.cloudray.scalapress.plugin.d3cloud

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.ScalapressRequest
import javax.persistence.{Table, Entity}
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_d3cloud_widget")
class D3CloudWidget extends Widget {

  override def backoffice = "/backoffice/widget/" + id
  override def render(req: ScalapressRequest): Option[String] = {
    Some(D3CloudWidget.script
      .replace("$WORDS",
      "Coldplay are a British rock band formed in 1996 by lead vocalist Chris Martin and lead guitarist Jonny Buckland at University College London.[4] After they formed under the name Pectoralz, Guy Berryman joined the group as a bassist and they changed their name to Starfish.[5] Will Champion joined as a drummer, backing vocalist, and multi-instrumentalist, completing the line-up. Manager Phil Harvey is often considered an unofficial fifth member.[6] The band renamed themselves Coldplay in 1998,[7] before recording and releasing three EPs")
      .split(" ").map('"' + _ + '"').mkString(","))
  }
}

object D3CloudWidget {
  val RESOURCE = "/com/cloudray/scalapress/plugin/d3cloud/d3cloud.js"
  val script = IOUtils.toString(getClass.getResourceAsStream(RESOURCE))
}