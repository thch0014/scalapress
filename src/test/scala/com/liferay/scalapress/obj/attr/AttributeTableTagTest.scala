package com.liferay.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.obj.tag.AttributeTableTag

/** @author Stephen Samuel */
class AttributeTableTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val av1 = new AttributeValue
    av1.attribute = new Attribute
    av1.attribute.id = 123
    av1.attribute.name = "band"
    av1.value = "coldplay"
    av1.attribute.public = true

    val av2 = new AttributeValue
    av2.attribute = new Attribute
    av2.attribute.id = 456
    av2.attribute.name = "singer"
    av2.value = "chris martin"
    av2.attribute.public = true

    val av3 = new AttributeValue
    av3.attribute = new Attribute
    av3.attribute.id = 63
    av3.attribute.name = "drummer"
    av3.value = "will champion"
    av3.attribute.public = false

    val obj = new Obj
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)
    obj.attributeValues.add(av3)

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withObject(obj)

    test("table includes only public attributes") {
        val actual = new AttributeTableTag().render(sreq, Map.empty).get
        assert(actual.contains("band"))
        assert(actual.contains("coldplay"))
        assert(actual.contains("singer"))
        assert(actual.contains("chris martin"))
        assert(!actual.contains("drummer"))
        assert(!actual.contains("will champion"))
    }

    test("exclude param removes attributes") {
        val actual = new AttributeTableTag().render(sreq, Map("exclude" -> "456")).get
        assert(actual.contains("band"))
        assert(actual.contains("coldplay"))
        assert(!actual.contains("singer"))
        assert(!actual.contains("chris martin"))
    }

    test("include param implicitly excludes anything not included") {
        val actual = new AttributeTableTag().render(sreq, Map("include" -> "456")).get
        assert(!actual.contains("band"))
        assert(!actual.contains("coldplay"))
        assert(actual.contains("singer"))
        assert(actual.contains("chris martin"))
    }
}
