package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.mail.MailSender
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{ShoppingPlugin, ShoppingPluginDao}
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.account.Account
import org.mockito.Mockito

/** @author Stephen Samuel */
class OrderCustomerNotificationSpringTest extends FunSuite with MockitoSugar {

  val appContext = new ClassPathXmlApplicationContext("/spring-mail-test.xml")
  val mailSender = appContext.getBean(classOf[MailSender])

  val context = new ScalapressContext
  context.installationDao = mock[InstallationDao]

  val installation = new Installation
  installation.domain = "globalgps.co.uk"
  installation.name = "big man tshirts"
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val plugin = new ShoppingPlugin
  plugin.orderConfirmationMessageBody = "Thanks for the order"
  plugin.orderConfirmationBcc = ""
  val shoppingPluginDao = mock[ShoppingPluginDao]
  Mockito.when(shoppingPluginDao.get).thenReturn(plugin)

  val service = new OrderCustomerNotificationService(mailSender, context, shoppingPluginDao)

  val order = new Order
  order.id = 151
  order.account = new Account
  order.account.email = "sam@sksamuel.com"

//  test("happy path sending") {
//    service.orderConfirmation(order)
//  }
}
