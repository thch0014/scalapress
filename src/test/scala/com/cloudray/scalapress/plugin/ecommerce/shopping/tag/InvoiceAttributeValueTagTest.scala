package com.cloudray.scalapress.plugin.ecommerce.shopping.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, Order}
import com.cloudray.scalapress.plugin.ecommerce.tags.InvoiceAttributeValueTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.{Item, ItemDao}
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.item.attr.{AttributeType, Attribute, AttributeValue}
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class InvoiceAttributeValueTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val order = new Order
  order.id = 51
  order.vatable = true

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.attributeType = AttributeType.Text
  av1.attribute.name = "band"
  av1.value = "coldplay"

  val item = new Item
  item.id = 66
  item.name = "my lovely object"
  item.attributeValues.add(av1)

  val line1 = new OrderLine
  line1.description = "big tshirt"
  line1.qty = 2
  line1.price = 1000
  line1.vatRate = 15.00
  line1.order = order
  line1.item = item.id

  val tag = new InvoiceAttributeValueTag()

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext()
  context.itemDao = mock[ItemDao]
  val sreq = new ScalapressRequest(req, context).withOrderLine(line1)

  Mockito.doReturn(item).when(context.itemDao).find(Matchers.anyLong)

  test("tag renders attribute value from parameter id") {
    val actual = tag.render(sreq, Map("id" -> "123"))
    assert("coldplay" === actual.get)
  }
}
