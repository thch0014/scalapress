package com.liferay.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import tag.{LocationTag, DistanceTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class DistanceTagTest extends FunSuite with MockitoSugar {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]

    test("distance happy path london to stockton") {

        val av = new AttributeValue
        av.value = "SW10"
        av.attribute = new Attribute
        av.attribute.attributeType = AttributeType.Postcode

        val obj = new Obj
        obj.attributeValues.add(av)

        val sreq = new ScalapressRequest(req, context).withObject(obj).withLocation("TS19")
        val render = DistanceTag.render(sreq, context, Map.empty)
        assert(render.get === "218.46")
    }

    test("distance happy path london to reading") {

        val av = new AttributeValue
        av.value = "SW10"
        av.attribute = new Attribute
        av.attribute.attributeType = AttributeType.Postcode

        val obj = new Obj
        obj.attributeValues.add(av)

        val sreq = new ScalapressRequest(req, context).withObject(obj).withLocation("rg10")
        val render = DistanceTag.render(sreq, context, Map.empty)
        assert(render.get === "32.85")
    }

    test("location happy path") {

        val obj = new Obj
        val sreq = new ScalapressRequest(req, context).withObject(obj).withLocation("TS19")
        val render = LocationTag.render(sreq, context, Map.empty)
        assert(render.get === "TS19")
    }
}
