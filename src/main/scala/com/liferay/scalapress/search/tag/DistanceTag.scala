package com.liferay.scalapress.search.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest, Tag}
import com.liferay.scalapress.obj.attr.AttributeFuncs
import com.liferay.scalapress.util.geo.Postcode
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
@Tag("distance")
class DistanceTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.obj.flatMap(AttributeFuncs.locationValue(_).flatMap(Postcode.gps(_))) match {
            case None => None
            case Some(coord1) => {
                request.location.flatMap(Postcode.gps(_)) match {
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
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        super.build(request.location, params)
    }
}