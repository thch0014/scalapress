package com.cloudray.scalapress.plugin.d3cloud

import com.cloudray.scalapress.widgets.Widget
import javax.persistence.{Table, Entity}
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_d3cloud_widget")
class D3CloudWidget extends Widget {

  override def backoffice = "/backoffice/widget/" + id
  override def render(req: ScalapressRequest): Option[String] = {
    getWords(req) match {
      case Some(words) =>
        val array = words.map('"' + _ + '"').mkString(",")
        Some("<script>" + D3CloudWidget.script.replace("$WORDS", array).replace("$ANGLE", "30") + "</script>")
      case None => None
    }
  }

  def getWords(req: ScalapressRequest) = {
    req
      .item
      .map(_
      .content
      .replaceAll("<.*?>", "")
      .replace("\"", "\\\"")
      .split(" ")
      .filterNot(arg => D3CloudWidget.STOP_WORDS.contains(arg.toLowerCase))
      .groupBy(e => e)
      .map(e => e._1 -> e._2.length)
      .toSeq
      .sortBy(_._2)
      .reverse
      .map(_._1)
      .take(D3CloudWidget.MAX_WORDS))
  }
}

object D3CloudWidget {
  val STOP_WORDS = Array("and",
    "of",
    "is",
    "to",
    "an",
    "with",
    "on",
    "or",
    "the",
    "and",
    "a",
    "it",
    "be",
    "also",
    "for",
    "new",
    "me",
    "them",
    "their",
    "your",
    "i",
    "as",
    "you",
    "then")
  val RESOURCE = "/com/cloudray/scalapress/plugin/d3cloud/d3cloud.js"
  val script = IOUtils.toString(getClass.getResourceAsStream(RESOURCE))
  val MAX_WORDS = 50
}