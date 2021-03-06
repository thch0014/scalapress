package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{Order, ShoppingPluginDao, ShoppingPlugin}

/** @author Stephen Samuel */
class OrderAdminNotificationServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val mailSender = mock[MailSender]
  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]

  val shoppingPluginDao = mock[ShoppingPluginDao]
  val service = new OrderAdminNotificationService(mailSender, context, shoppingPluginDao)

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

  test("when shopping settings has receipents set then a message is sent via mail sender") {
    plugin.orderConfirmationRecipients = "admin@sammy.com"
    service.orderConfirmed(order)
    Mockito.verify(mailSender).send(Matchers.any[SimpleMailMessage])
  }

  test("that receipients for the message are taken from the shopping settings") {
    plugin.orderConfirmationRecipients = "admin@sammy.com,gareth@southgate.com"
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service.orderConfirmed(order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getTo === Array("admin@sammy.com", "gareth@southgate.com"))
  }

  test("that the email sender uses the recipients") {
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service._send(Array("admin@sammy.com", "elvis@graceland.com"), order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getTo === Array("admin@sammy.com", "elvis@graceland.com"))
  }

  test("that the message uses the installation as the sender details") {
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    service._send(Array("admin@sammy.com", "elvis@graceland.com"), order)
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getFrom === "big man tshirts <donotreply@coldplay.com>")
  }
}
