package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{ObjectType, TypeDao}
import org.mockito.Mockito
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class TypesInterceptorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[TypeDao]
    val interceptor = new TypesInterceptor(dao)

    test("types do not include deleted") {
        val type1 = new ObjectType
        type1.deleted = false
        val type2 = new ObjectType
        type2.deleted = true
        Mockito.when(dao.findAll()).thenReturn(List(type1, type2))

        val req = mock[HttpServletRequest]
        val resp = mock[HttpServletResponse]
        val model = new ModelAndView
        interceptor.postHandle(req, resp, null, model)

        val types = model.getModelMap.get("types").asInstanceOf[util.Collection[ObjectType]].asScala.toSeq
        assert(1 === types.size)
        assert(type1 === types(0))
    }

}
