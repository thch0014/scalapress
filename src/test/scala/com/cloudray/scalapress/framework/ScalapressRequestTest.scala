package com.cloudray.scalapress.framework

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.ecommerce.domain.Basket
import org.mockito.{ArgumentCaptor, Mockito}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.BasketDao

/** @author Stephen Samuel */
class ScalapressRequestTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = ScalapressRequest(req, context)

  val dao = mock[BasketDao]
  Mockito.when(context.bean[BasketDao]).thenReturn(dao)
  Mockito.doReturn("abc").when(req).getAttribute(ScalapressConstants.RequestAttributeKey_SessionId)

  "a basket lookup" should "return a basket stored in the http request" in {
    val basket = new Basket
    Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.RequestAttributeKey_Basket)
    assert(sreq.basket === basket)
  }

  it should "retrieve from dao when no basket is stored in the request" in {
    val basket = new Basket
    Mockito.doReturn(basket).when(req).getAttribute(ScalapressConstants.RequestAttributeKey_Basket)
    Mockito.when(dao.get("abc")).thenReturn(Some(basket))
    assert(sreq.basket === basket)
  }

  it should "create and persist basket when basket does not exist" in {
    Mockito.when(dao.get("abc")).thenReturn(None)
    val captor = ArgumentCaptor.forClass(classOf[Basket])
    val basket = sreq.basket
    Mockito.verify(dao).save(captor.capture)
    assert(basket === captor.getValue)
  }

  "a session lookup" should "use the session id from constants" in {
    assert(sreq.sessionId === "abc")
  }

}
