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
class OrderAdminNotificationServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val service = new OrderAdminNotificationService
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

    test("when shopping settings has receipents set then a message is sent via mail sender") {
        plugin.orderConfirmationRecipients = "admin@sammy.com"
        service.orderConfirmed(order)
        Mockito.verify(service.mailSender).send(Matchers.any[SimpleMailMessage])
    }

    test("that receipients for the message are taken from the shopping settings") {
        plugin.orderConfirmationRecipients = "admin@sammy.com,gareth@southgate.com"
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderConfirmed(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getTo === Array("admin@sammy.com", "gareth@southgate.com"))
    }

    test("that the email sender uses the recipients") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service._send(Array("admin@sammy.com", "elvis@graceland.com"), order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getTo === Array("admin@sammy.com", "elvis@graceland.com"))
    }

    test("that the message uses the installation as the sender details") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service._send(Array("admin@sammy.com", "elvis@graceland.com"), order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getFrom === "big man tshirts <donotreply@coldplay.com>")
    }
}
