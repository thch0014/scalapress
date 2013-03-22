package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.joda.time.DateTime
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.tag.AttributeValueRenderer
import com.liferay.scalapress.theme.MarkupRenderer

/** @author Stephen Samuel */
object InvoiceAccountNumberTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.id.toString)
    }
}

object InvoiceAccountNameTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.name)
    }
}

object InvoiceAccountEmailTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.email)
    }
}

object InvoiceDeliveryAddressTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.flatMap(order => Option(order.deliveryAddress)).map(add => {
            val label = add.label
            label
        })
    }
}

object InvoiceBillingAddressTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.flatMap(order => Option(order.billingAddress)).map(_.label)
    }
}

object InvoiceDateTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(arg => new DateTime(arg.datePlaced).toString("dd/MM/yyyy"))
    }
}

object InvoiceAttributeValueTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => Some("<!-- no id specified for attribute tag -->")
            case Some(id) => {
                request.orderLine.flatMap(line => Option(context.objectDao.find(line.obj))).flatMap(obj => {
                    obj.attributeValues.asScala.find(_.attribute.id == id.trim.toLong) match {
                        case None => None
                        case Some(av) => Some(build(AttributeValueRenderer.renderValue(av), params))
                    }
                })
            }
        }
    }
}

object InvoiceLineQtyTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.orderLine.map(_.qty.toString)
    }
}

import scala.collection.JavaConverters._

object InvoiceLinesTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.order.flatMap(order => {
            Option(context.shoppingPluginDao.get.invoiceLineMarkup) match {
                case None => None
                case Some(m) =>
                    val render = MarkupRenderer.renderOrderLines(order.lines.asScala, m, request)
                    Some(render)
            }
        })
    }
}

object InvoiceNumberTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.order.map(order => order.id.toString)
    }
}

object InvoiceDeliveryDetailsTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.order.flatMap(order => Option(order.deliveryDetails))
    }
}

object InvoiceDeliveryChargeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.order.map(order => {
            val text = if (params.contains("ex"))
                order.deliveryEx
            else if (params.contains("vat"))
                order.deliveryVat
            else
                order.deliveryInc
            val textFormatted = "&pound;%1.2f".format(text)
            build(textFormatted, params)
        })
    }
}

object InvoiceLineDescTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.orderLine.map(line => line.description)
    }
}

object InvoiceLinePriceTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.orderLine.map(line => {
            val text = if (params.contains("ex"))
                line.priceExVat
            else if (params.contains("vat"))
                line.priceVat
            else
                line.priceIncVat
            val textFormatted = "&pound;%1.2f".format(text)
            build(textFormatted, params)
        })
    }
}

object InvoiceLineTotalTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.orderLine.map(line => {
            val text = if (params.contains("ex"))
                line.totalExVat
            else if (params.contains("vat"))
                line.totalVat
            else
                line.totalIncVat
            val textFormatted = "&pound;%1.2f".format(text)
            build(textFormatted, params)
        })
    }

    override def tags = Array("basket_line_total")
}

object InvoiceTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(order => {
            val text = if (params.contains("ex"))
                order.subtotal
            else if (params.contains("vat"))
                order.vat
            else
                order.total
            val textFormatted = "&pound;%1.2f".format(text)
            build(textFormatted, params)
        })
    }
}

object InvoiceLinesTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(order => {
            val text = if (params.contains("ex"))
                order.linesSubtotal
            else if (params.contains("vat"))
                order.linesVat
            else
                order.linesTotal
            val textFormatted = "&pound;%1.2f".format(text)
            build(textFormatted, params)
        })
    }
}