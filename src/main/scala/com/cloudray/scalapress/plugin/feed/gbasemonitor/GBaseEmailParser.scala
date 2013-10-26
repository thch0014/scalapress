package com.cloudray.scalapress.plugin.feed.gbasemonitor

/** @author Stephen Samuel */
trait GBaseResultParser

class GBaseResultEmailParser extends GBaseResultParser {

  val itemsPattern = "(\\d+) of (\\d+) items inserted".r

  def parse(text: String): GBaseResult = {
    itemsPattern findFirstIn text match {
      case Some(itemsPattern(inserted, total)) => GBaseResult(inserted.toInt, total.toInt)
      case None => GBaseResult(0, 0)
    }
  }
}

case class GBaseResult(inserted: Int, total: Int)
