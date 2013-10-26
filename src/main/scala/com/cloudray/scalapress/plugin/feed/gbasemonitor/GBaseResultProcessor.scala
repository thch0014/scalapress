package com.cloudray.scalapress.plugin.feed.gbasemonitor

import org.apache.camel.{Exchange, Processor}

/** @author Stephen Samuel */
class GBaseResultProcessor(minItems: Int) extends Processor {

  def isGoodResult(result: GBaseResult): Boolean =
    result.inserted >= minItems && result.inserted.toDouble / result.total.toDouble > 0.90d

  def process(exchange: Exchange) {
    val body = exchange.getIn.getBody.toString
    val result = new GBaseResultEmailParser().parse(body)
    if (isGoodResult(result)) {
      exchange.setProperty(Exchange.ROUTE_STOP, "true")
    } else {
      exchange.getIn.setBody(result)
    }
  }
}
