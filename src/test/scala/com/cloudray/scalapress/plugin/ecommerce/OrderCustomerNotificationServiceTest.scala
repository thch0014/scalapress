package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import org.mockito.{ArgumentCaptor, Matchers, Mockito}

/** @author Stephen Samuel */
class OrderCustomerNotificationServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val service = new OrderCustomerNotificationService
    service.mailSender = mock[MailSender]
    service.context = new ScalapressContext
    service.context.installationDao = mock[InstallationDao]
    service.shoppingPluginDao = mock[ShoppingPluginDao]

    val installation = new Installation
    installation.domain = "coldplay.com"
    installation.name = "big man tshirts"

    val plugin = new ShoppingPlugin

    Mockito.when(service.context.installationDao.get).thenReturn(installation)
    Mockito.when(service.shoppingPluginDao.get).thenReturn(plugin)

    val order = new Order
    order.id = 151
    order.account = new Obj
    order.account.email = "kirk@enterprise.com"

    test("that a message is sent via mail sender") {
        service.orderCompleted(order)
        Mockito.verify(service.mailSender).send(Matchers.any[SimpleMailMessage])
    }

    test("that the message uses the account email as recipient") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service._send(order, "body")
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getTo === Array("kirk@enterprise.com"))
    }

    test("that the message uses the installation as the sender details") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service._send(order, "body")
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getFrom === "big man tshirts <donotreply@coldplay.com>")
    }

    test("that the message subject contains the order id") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service._send(order, "body")
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getSubject.contains("#" + order.id))
    }

    test("that a confirmation email uses the specified body") {
        plugin.orderConfirmationMessageBody = "lovely order that"
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderConfirmation(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert("lovely order that" === msg.getText)
    }

    test("that a completed email uses the specified body") {
        plugin.orderConfirmationMessageBody = "all done and dusted"
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderCompleted(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert("all done and dusted" === msg.getText)
    }

    test("that the message body is marked-up before sending") {
        plugin.orderConfirmationMessageBody = "lovely order is #[order_id]"
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderConfirmation(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert("lovely order is #151" === msg.getText)
    }
}
