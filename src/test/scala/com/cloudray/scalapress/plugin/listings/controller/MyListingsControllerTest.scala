package com.cloudray.scalapress.plugin.listings.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import com.cloudray.scalapress.obj.{ObjectDao, Obj, ObjectQuery}
import com.cloudray.scalapress.security.SecurityResolver
import com.cloudray.scalapress.theme.ThemeService
import com.sksamuel.scoot.soa.Page

/** @author Stephen Samuel */
class MyListingsControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new MyListingsController
    controller.context = new ScalapressContext
    controller.securityResolver = mock[SecurityResolver]
    controller.context.objectDao = mock[ObjectDao]
    controller.themeService = mock[ThemeService]

    val req = mock[HttpServletRequest]
    val user = new Obj
    user.id = 53

    Mockito.when(controller.securityResolver.getUser(req)).thenReturn(Some(user))

    test("controller looks up listings by account id") {
        Mockito.when(controller.context.objectDao.search(Matchers.any[ObjectQuery])).thenReturn(Page.empty[Obj])
        controller.list(req)
        val captor = ArgumentCaptor.forClass(classOf[ObjectQuery])
        Mockito.verify(controller.context.objectDao).search(captor.capture)
        val q = captor.getValue
        assert(53 === q.accountId.get)
    }
}
