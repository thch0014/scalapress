package com.liferay.scalapress.theme.tag

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import org.joda.time.DateTime

/** @author Stephen Samuel */
@Tag("date")
class DateTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val format = params.get("format").getOrElse("dd/MM/yyyy")
        Some(new DateTime().toString(format))
    }
}

object DateCreatedTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val format = params.get("format").getOrElse("dd/MM/yyyy")

        val timestamp = request.obj.map(_.dateCreated).orElse(request.folder.map(_.dateCreated))
        timestamp match {
            case None => None
            case Some(t) => {
                val formatted = new DateTime(t).toString(format)
                Option(formatted)
            }
        }
    }
}