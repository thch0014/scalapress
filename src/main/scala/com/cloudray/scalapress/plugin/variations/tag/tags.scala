package com.cloudray.scalapress.plugin.variations.tag

import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.plugin.variations.VariationDao
import scala.xml.Utility

/** @author Stephen Samuel */
@Tag("variations_select")
class VariationsSelectTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.flatMap(obj => {

      val variations = request.context.bean[VariationDao].findByObjectId(obj.id)
      variations.size match {
        case 0 => None
        case _ =>

          val options = variations.map(v =>
            <option>
              {v.name}
            </option>)

          val xml = <select name="variation">
            {options}
          </select>

          Some(Utility.trim(xml).toString())
      }
    })
  }
}
