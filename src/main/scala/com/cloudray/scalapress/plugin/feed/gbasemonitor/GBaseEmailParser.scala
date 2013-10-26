package com.cloudray.scalapress.plugin.feed.gbasemonitor

/** @author Stephen Samuel */
trait GBaseResultParser

class GBaseResultEmailParser extends GBaseResultParser {

  val itemsPattern = "(\\d+) of (\\d+) items inserted".r

  def parse(text: String): Option[GBaseResult] = {
    itemsPattern findFirstIn text match {
      case Some(itemsPattern(inserted, total)) => Some(GBaseResult(inserted.toInt, total.toInt))
      case None => None
    }
  }
}

case class GBaseResult(inserted: Int, total: Int)
