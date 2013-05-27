package com.liferay.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import org.mockito.{ArgumentCaptor, Matchers, Mockito}

/** @author Stephen Samuel */
class OrderCustomerNotificationServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val service = new OrderCustomerNotificationService
    service.mailSender = mock[MailSender]
    service.context = new ScalapressContext
    service.context.installationDao = mock[InstallationDao]

    val installation = new Installation
    installation.domain = "coldplay.com"
    installation.name = "big man tshirts"
    Mockito.when(service.context.installationDao.get).thenReturn(installation)

    val order = new Order
    order.id = 151
    order.account = new Obj
    order.account.email = "kirk@enterprise.com"

    test("that a message is sent via mail sender") {
        service.orderPlaced(order)
        Mockito.verify(service.mailSender).send(Matchers.any[SimpleMailMessage])
    }

    test("that the message uses the account email as recipient") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderPlaced(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getTo === Array("kirk@enterprise.com"))
    }

    test("that the message uses the installation as the sender details") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderPlaced(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getFrom === "big man tshirts <donotreply@coldplay.com>")
    }

    test("that the message subject contains the order id") {
        val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
        service.orderPlaced(order)
        Mockito.verify(service.mailSender).send(captor.capture)
        val msg = captor.getValue
        assert(msg.getSubject.contains("#" + order.id))
    }
}
