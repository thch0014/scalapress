package com.cloudray.scalapress.plugin.listings.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import com.cloudray.scalapress.item.{ItemDao, Item, ItemQuery}
import com.cloudray.scalapress.security.SecurityResolver
import com.cloudray.scalapress.theme.ThemeService
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class MyListingsControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val themeService = mock[ThemeService]
  val context = new ScalapressContext
  context.itemDao = mock[ItemDao]

  val controller = new MyListingsController(context, themeService)
  controller.securityResolver = mock[SecurityResolver]

  val req = mock[HttpServletRequest]
  val acc = new Account
  acc.id = 53

  Mockito.when(controller.securityResolver.getAccount(req)).thenReturn(Some(acc))

  test("controller looks up listings by account id") {
    Mockito.when(context.itemDao.search(Matchers.any[ItemQuery])).thenReturn(Page.empty[Item])
    controller.list(req)
    val captor = ArgumentCaptor.forClass(classOf[ItemQuery])
    Mockito.verify(context.itemDao).search(captor.capture)
    val q = captor.getValue
    assert(53 === q.accountId.get)
  }
}
