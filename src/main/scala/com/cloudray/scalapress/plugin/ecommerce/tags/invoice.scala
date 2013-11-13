package com.cloudray.scalapress.plugin.ecommerce.tags

import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.ecommerce.ShoppingPluginDao
import com.cloudray.scalapress.item.attr.AttributeValueRenderer
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("invoice_account_number")
class InvoiceAccountNumberTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(_.account.id.toString)
  }
}

@Tag("invoice_account_name")
class InvoiceAccountNameTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(_.account.name)
  }
}

@Tag("invoice_account_email")
class InvoiceAccountEmailTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(_.account.email)
  }
}

@Tag("invoice_delivery_address")
class InvoiceDeliveryAddressTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.flatMap(order => Option(order.deliveryAddress)).map(add => {
      val label = add.label
      label
    })
  }
}

@Tag("invoice_billing_address")
class InvoiceBillingAddressTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.flatMap(order => Option(order.billingAddress)).map(_.label)
  }
}

@Tag("invoice_date")
class InvoiceDateTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order
      .map(arg => new DateTime(arg.datePlaced, DateTimeZone.forID("Europe/London")).toString("dd/MM/yyyy"))
  }
}

@Tag("invoice_customer_note")
class InvoiceCustomerNoteTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.flatMap(o => Option(o.customerNote))
  }
}

@Tag("invoice_attribute_value")
class InvoiceAttributeValueTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    params.get("id") match {
      case None => Some("<!-- no id specified for attribute tag -->")
      case Some(id) => {
        request.orderLine.flatMap(line => Option(request.context.itemDao.find(line.item))).flatMap(obj => {
          obj.attributeValues.asScala.find(_.attribute.id == id.trim.toLong) match {
            case None => None
            case Some(av) => Some(build(AttributeValueRenderer.renderValue(av), params))
          }
        })
      }
    }
  }
}

@Tag("invoice_line_qty")
class InvoiceLineQtyTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.orderLine.map(_.qty.toString)
  }
}

@Tag("invoice_lines")
class InvoiceLinesTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.order.flatMap(order => {
      Option(request.context.bean[ShoppingPluginDao].get.invoiceLineMarkup) match {
        case None => None
        case Some(m) =>
          val render = MarkupRenderer.renderOrderLines(order.sortedLines, m, request)
          Some(render)
      }
    })
  }
}

@Tag("invoice_number")
class InvoiceNumberTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(order => order.id.toString)
  }
}

@Tag("invoice_delivery_desc")
class InvoiceDeliveryDetailsTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.flatMap(order => Option(order.deliveryDetails))
  }
}

@Tag("invoice_delivery_charge")
class InvoiceDeliveryChargeTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.order.map(order => {
      val text = if (params.contains("ex")) order.deliveryEx
      else if (params.contains("vat") && request.installation.vatEnabled) order.deliveryVat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) order.deliveryInc
      else order.deliveryEx
      val textFormatted = "&pound;%1.2f".format(text)
      build(textFormatted, params)
    })
  }
}

@Tag("invoice_line_desc")
class InvoiceLineDescTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.orderLine.map(line => line.description)
  }
}

@Tag("invoice_line_price")
class InvoiceLinePriceTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.orderLine.map(line => {
      val text = if (params.contains("ex")) line.priceExVat
      else if (params.contains("vat") && request.installation.vatEnabled) line.priceVat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) line.priceIncVat
      else line.priceExVat
      val textFormatted = "&pound;%1.2f".format(text)
      build(textFormatted, params)
    })
  }
}

@Tag("invoice_line_total")
class InvoiceLineTotalTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.orderLine.map(line => {
      val text = if (params.contains("ex")) line.totalExVat
      else if (params.contains("vat") && request.installation.vatEnabled) line.totalVat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) line.totalIncVat
      else line.totalExVat
      val textFormatted = "&pound;%1.2f".format(text)
      build(textFormatted, params)
    })
  }
}

@Tag("invoice_total")
class InvoiceTotalTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(order => {
      val amount = if (params.contains("ex")) order.subtotal
      else if (params.contains("vat") && request.installation.vatEnabled) order.vat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) order.total
      else order.subtotal
      val formatted = "&pound;%1.2f".format(amount)
      build(formatted, params)
    })
  }
}

@Tag("invoice_lines_total")
class InvoiceLinesTotalTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.order.map(order => {
      val text = if (params.contains("ex")) order.linesSubtotal
      else if (params.contains("vat") && request.installation.vatEnabled) order.linesVat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) order.linesTotal
      else order.linesSubtotal
      val textFormatted = "&pound;%1.2f".format(text)
      build(textFormatted, params)
    })
  }
}