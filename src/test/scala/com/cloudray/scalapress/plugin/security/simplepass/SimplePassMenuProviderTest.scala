package com.cloudray.scalapress.plugin.security.simplepass

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito

/** @author Stephen Samuel */
class SimplePassMenuProviderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val provider = new SimplePassMenuProvider
  val context = mock[ScalapressContext]

  test("when simple pass is not enabled then menu provider should return no menu items") {
    Mockito.when(context.beans[SimplePassInterceptor]).thenReturn(Nil)
    val menu = provider.menu(context)
    assert(menu._2.isEmpty)
  }
}
