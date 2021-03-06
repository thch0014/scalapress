package com.cloudray.scalapress.search.tag

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.{AttributeType, AttributeValue, Attribute}
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class DistanceTagTest extends FunSuite with MockitoSugar {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]

  test("distance happy path london to stockton") {

    val av = new AttributeValue
    av.value = "SW10"
    av.attribute = new Attribute
    av.attribute.attributeType = AttributeType.Postcode

    val obj = new Item
    obj.attributeValues.add(av)

    val sreq = new ScalapressRequest(req, context).withItem(obj).withLocation("TS19")
    val render = new DistanceTag().render(sreq, Map.empty)
    assert(render.get === "218.46")
  }

  test("distance happy path london to reading") {

    val av = new AttributeValue
    av.value = "SW10"
    av.attribute = new Attribute
    av.attribute.attributeType = AttributeType.Postcode

    val obj = new Item
    obj.attributeValues.add(av)

    val sreq = new ScalapressRequest(req, context).withItem(obj).withLocation("rg10")
    val render = new DistanceTag().render(sreq, Map.empty)
    assert(render.get === "32.85")
  }

  test("location happy path") {

    val obj = new Item
    val sreq = new ScalapressRequest(req, context).withItem(obj).withLocation("TS19")
    val render = new LocationTag().render(sreq, Map.empty)
    assert(render.get === "TS19")
  }
}
