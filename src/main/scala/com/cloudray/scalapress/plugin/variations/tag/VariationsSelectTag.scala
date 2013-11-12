package com.cloudray.scalapress.plugin.variations.tag

import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.cloudray.scalapress.plugin.variations.{Dimension, Variation, DimensionDao, VariationDao}
import scala.collection.JavaConverters._
import scala.xml.{Utility, Node}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("variations_select")
class VariationsSelectTag extends ScalapressTag with TagBuilder {

  // find possible values for a given dimension
  def uniqueValuesForDimension(dimension: Dimension, variations: Iterable[Variation]): Seq[String] = {
    variations
      .flatMap(_.dimensionValues.asScala)
      .filter(_.dimension.id == dimension.id)
      .map(_.value)
      .filterNot(_.isEmpty)
      .toSet.toSeq.sorted
  }

  def values2options(values: Seq[String]): Seq[Node] =
    values.map(value => Utility.trim(<option>
      {value}
    </option>))

  def renderDimension(dimension: Dimension, variations: Iterable[Variation]): Option[Node] = {
    val values = uniqueValuesForDimension(dimension, variations)
    if (values.size == 0) None
    else selectTag(dimension, values2options(values))
  }

  def selectTag(dimension: Dimension, options: Seq[Node]) = {
    val name = s"dimension_${dimension.id}"
    val xml = <select name={name}>
      {options}
    </select>
    Some(Utility.trim(xml))
  }

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.flatMap(obj => {

      val variations = request.context.bean[VariationDao].findByObjectId(obj.id)
      val dimensions = request.context.bean[DimensionDao].findByObjectType(obj.itemType.id)

      variations.size match {
        case 0 => None
        case _ =>
          val selects = dimensions.flatMap(renderDimension(_, variations))
          Some(selects.mkString("\n"))
      }
    })
  }
}
