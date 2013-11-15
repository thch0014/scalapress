package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import java.util
import scala.collection.JavaConverters._
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao}

/** @author Stephen Samuel */
class AccountTypesInterceptorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[AccountTypeDao]
  val interceptor = new AccountTypesInterceptor(dao)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]
  val model = new ModelAndView

  val type1 = new AccountType
  type1.name = "trade accounts"

  val type2 = new AccountType
  type2.name = "retail account"

  Mockito.when(dao.findAll).thenReturn(List(type1, type2))

  test("interceptor includes all types") {
    interceptor.postHandle(req, resp, null, model)
    val types = model.getModelMap.get("accountTypes").asInstanceOf[util.Collection[AccountType]].asScala.toSeq
    assert(2 === types.size)
    assert(type1 === types(0))
    assert(type2 === types(1))
  }
}
