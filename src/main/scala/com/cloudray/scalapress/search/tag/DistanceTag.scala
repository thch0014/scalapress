package com.cloudray.scalapress.search.tag

import com.cloudray.scalapress.item.attr.AttributeFuncs
import com.cloudray.scalapress.util.geo.Postcode
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("distance")
class DistanceTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.flatMap(AttributeFuncs.locationValue(_).flatMap(Postcode.gps)) match {
      case None => None
      case Some(coord1) => {
        request.location.flatMap(Postcode.gps) match {
          case None => None
          case Some(coord2) => {
            val distance = coord1.distance(coord2) * 0.621371192
            val formatted = "%.2f".format(distance)
            Some(super.build(formatted, params))
          }
        }
      }
    }
  }
}

@Tag("location")
class LocationTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    super.build(request.location, params)
  }
}