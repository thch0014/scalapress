package com.cloudray.scalapress.item.controller

import org.scalatest.{ShouldMatchers, OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.item.{ItemDao, ItemType, Item}
import com.cloudray.scalapress.util.mvc.NotFoundException
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.ScalapressContext
import org.mockito.Mockito

/** @author Stephen Samuel */
class ItemControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest with ShouldMatchers {

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val item = new Item
  item.itemType = new ItemType
  item.itemType.name = "product"

  val itemDao = mock[ItemDao]
  val themeService = mock[ThemeService]
  val context = mock[ScalapressContext]

  val interceptor1 = mock[ItemInterceptor]
  val interceptor2 = mock[ItemInterceptor]
  Mockito.when(interceptor1.preHandle(item, req, resp)).thenReturn(true)
  Mockito.when(interceptor2.preHandle(item, req, resp)).thenReturn(true)
  val interceptors = List(interceptor1, interceptor2)
  Mockito.when(context.beans[ItemInterceptor]).thenReturn(interceptors)

  val controller = new ItemController(itemDao, themeService, context)

  "an item controller" should "throw an exception for hidden object types" in {
    evaluating {
      item.itemType.hidden = true
      controller.view(item, req, resp)
    } should produce[NotFoundException]
  }

  it should "call all pre handling interceptors" in {
    controller.view(item, req, resp)
    Mockito.verify(interceptor1).preHandle(item, req, resp)
    Mockito.verify(interceptor2).preHandle(item, req, resp)
  }

  it should "call all post handling interceptors" in {
    controller.view(item, req, resp)
    Mockito.verify(interceptor1).postHandle(item, req, resp)
    Mockito.verify(interceptor2).postHandle(item, req, resp)
  }

  it should "stop execution if an interceptor pre call returns false" in {
    Mockito.when(interceptor1.preHandle(item, req, resp)).thenReturn(false)
    evaluating {
      controller.view(item, req, resp)
    } should produce[ItemInterceptorException]
  }
}
