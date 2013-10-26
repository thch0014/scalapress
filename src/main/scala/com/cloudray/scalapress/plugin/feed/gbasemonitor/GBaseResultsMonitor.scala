package com.cloudray.scalapress.plugin.feed.gbasemonitor

import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.springframework.mail.MailSender
import org.springframework.beans.factory.annotation.{Value, Autowired}
import javax.annotation.{PostConstruct, PreDestroy}

/** @author Stephen Samuel */
class GBaseResultsMonitor {

  @Autowired
  var mailSender: MailSender = _

  @Value("${gbase.monitor.endpoint}")
  var endpoint: String = _

  @Value("${gbase.monitor.notify.email}")
  var notifyEmail: String = _

  @Value("${gbase.monitor.check.items}")
  var minItems: Int = _

  val context = new DefaultCamelContext()
  context.setName("GBaseResultsMonitor")

  @PostConstruct
  def start() {
    context.addRoutes(new MonitorRoute(endpoint, minItems, notifyEmail, mailSender))
    context.start()
  }

  @PreDestroy
  def stop(): Unit = context.stop()
}

class MonitorRoute(endpoint: String,
                   minItems: Int,
                   notifyEmail: String,
                   mailSender: MailSender) extends RouteBuilder {
  endpoint
    .process(new GBaseResultProcessor(minItems))
    .process(new SendWarningProcessor(mailSender, notifyEmail))
}