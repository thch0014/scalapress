package com.cloudray.scalapress.search.tag

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.attr.{AttributeOption, Attribute, AttributeDao}
import org.mockito.Mockito
import com.cloudray.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class AttributeSearchTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext()
  context.attributeDao = mock[AttributeDao]

  val req = mock[HttpServletRequest]

  val sreq = ScalapressRequest(req, context)

  val tag = new AttributeSearchTag

  "an attribute search tag" should "render using the id param" in {

    val option = new AttributeOption
    option.value = "coldplay"

    val a = new Attribute
    a.attributeType = AttributeType.Selection
    a.options.add(option)
    Mockito.when(context.attributeDao.find(9)).thenReturn(a)

    val actual = tag.render(sreq, Map("id" -> "9"))
    assert(
      """<form method="GET" action="/search"><select name="attr_9" action="/search"><option>coldplay</option></select><button type="submit">Go</button></form>""" === actual
        .get)
  }

  it should "render None when the attribute is not an attribute Selection type" in {

    val option = new AttributeOption
    option.value = "coldplay"

    val a = new Attribute
    a.attributeType = AttributeType.Text
    a.options.add(option)
    Mockito.when(context.attributeDao.find(9)).thenReturn(a)

    val actual = tag.render(sreq, Map("id" -> "9"))
    assert(actual.isEmpty)
  }

  it should "render None when the attribute id is not valid" in {

    val r = mock[ScalapressRequest]


    val option = new AttributeOption
    option.value = "coldplay"

    val a = new Attribute
    a.options.add(option)
    Mockito.when(r.attribute(9)).thenReturn(a)

    val actual = tag.render(sreq, Map("id" -> "149"))
    assert(actual.isEmpty)
  }
}
