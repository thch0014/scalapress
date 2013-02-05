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

        request.folder.map(_.dateCreated).orElse(request.obj.map(_.dateCreated)) match {
            case None => None
            case Some(timestamp) => Option(new SimpleDateFormat(format).format(timestamp))
        }
    }
}