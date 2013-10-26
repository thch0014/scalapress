package com.cloudray.scalapress.plugin.feed.gbasemonitor

import org.apache.camel.{Exchange, Processor}

/** @author Stephen Samuel */
class GBaseResultProcessor(minItems: Int) extends Processor {

  def isGoodResult(result: GBaseResult): Boolean =
    result.inserted >= minItems && result.inserted.toDouble / result.total.toDouble > 0.90d

  def process(exchange: Exchange) {
    val body = exchange.getIn.getBody.toString
    new GBaseResultEmailParser().parse(body) match {
      case Some(result) if isGoodResult(result) => exchange.setProperty(Exchange.ROUTE_STOP, "true")
      case Some(result) => exchange.getIn.setBody(result)
      case None => exchange.setProperty(Exchange.ROUTE_STOP, "true")
    }
  }
}
