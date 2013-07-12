package com.cloudray.scalapress.plugin.variations.tag

import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.plugin.variations.VariationDao
import scala.xml.Utility

/** @author Stephen Samuel */
@Tag("variations_select")
class VariationsSelectTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.map(obj => {

      val options = request.context.bean[VariationDao].findByObjectId(obj.id).map(v =>
        <option>
          {v.name}
        </option>)

      val xml = <select name="variation">
        {options}
      </select>

      Utility.trim(xml).toString()
    })
  }
}
