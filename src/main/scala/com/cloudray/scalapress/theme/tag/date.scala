package com.cloudray.scalapress.theme.tag

import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("date")
class DateTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    val format = params.get("format").getOrElse("dd/MM/yyyy")
    Some(new DateTime(DateTimeZone.UTC).toString(format))
  }
}

@Tag("date_created")
class DateCreatedTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    val format = params.get("format").getOrElse("dd/MM/yyyy")

    val timestamp = request.item.map(_.dateCreated).orElse(request.folder.map(_.dateCreated))
    timestamp match {
      case None => None
      case Some(t) => {
        val formatted = new DateTime(t, DateTimeZone.UTC).toString(format)
        Option(formatted)
      }
    }
  }
}