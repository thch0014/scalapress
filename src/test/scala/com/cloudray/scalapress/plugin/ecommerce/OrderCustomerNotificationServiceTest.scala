package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class OrderCustomerNotificationServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val mailSender = mock[MailSender]
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]
  val shoppingPluginDao = mock[ShoppingPluginDao]
  val service = new OrderCustomerNotificationService(mailSender, context, shoppingPluginDao)

  val installation = new Installation
  installation.domain = "coldplay.com"
  installation.name = "big man tshirts"

  val plugin = new ShoppingPlugin

  Mockito.when(context.installationDao.get).thenReturn(installation)
  Mockito.when(shoppingPluginDao.get).thenReturn(plugin)

  val order = new Order
  order.id = 151
  order.account = new Account
  order.account.email = "kirk@enterprise.com"

  test("no domain set uses localhost") {
    installation.domain = null
    plugin.orderCompletionMessageBody = "go go go"
    service.orderCompleted(order)

    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getReplyTo === "donotreply@localhost")
  }

  test("that a message is sent via mail sender") {
    plugin.orderCompletionMessageBody = "go go go"
    service.orderCompleted(order)
    Mockito.verify(mailSender).send(Matchers.any[SimpleMailMessage])
  }

  test("that the message uses the account email as recipient") {
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service._send(order, "body")
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getTo === Array("kirk@enterprise.com"))
  }

  test("that the message uses the installation as the sender details") {
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service._send(order, "body")
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getFrom === "big man tshirts <donotreply@coldplay.com>")
  }

  test("that the message subject contains the order id") {
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service._send(order, "body")
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getSubject.contains("#" + order.id))
  }

  test("that a confirmation email uses the specified body") {
    plugin.orderConfirmationMessageBody = "lovely order that"
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service.orderConfirmation(order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("lovely order that" === msg.getText)
  }

  test("that a completed email uses the specified body") {
    plugin.orderCompletionMessageBody = "all done and dusted"
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service.orderCompleted(order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("all done and dusted" === msg.getText)
  }

  test("that the message body is marked-up before sending") {
    plugin.orderConfirmationMessageBody = "lovely order is #[order_id]"
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service.orderConfirmation(order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("lovely order is #151" === msg.getText)
  }

  test("that the bcc fields are set if specified in the plugin") {
    plugin.orderConfirmationMessageBody = "lovely order is #[order_id]"
    plugin.orderConfirmationBcc = "sammy@sambo.com,chris@coldplay.com"
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service.orderConfirmation(order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(Array("sammy@sambo.com", "chris@coldplay.com") === msg.getBcc)
  }
}
