package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.joda.time.DateTime
import com.liferay.scalapress.service.theme.tag.ScalapressTag
import java.text.SimpleDateFormat

/** @author Stephen Samuel */
object DateTag extends ScalapressTag {
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