package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.joda.time.DateTime
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.service.FriendlyUrlGenerator

/** @author Stephen Samuel */
object InvoiceAccountNumberTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.id.toString)
    }
}

object InvoiceAccountNameTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.name.toString)
    }
}

object InvoiceAccountEmailTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.account.email.toString)
    }
}

object InvoiceDeliveryAddressTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.deliveryAddress.label)
    }
}

object InvoiceBillingAddressTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(_.billingAddress.label)
    }
}

object InvoiceDateTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.order.map(arg => new DateTime(arg.datePlaced).toString)
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
            val textFormatted = "£%1.2f".format(text / 100.0)
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
            val textFormatted = "£%1.2f".format(text / 100.0)
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
            val textFormatted = "£%1.2f".format(text / 100.0)
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
            val textFormatted = "£%1.2f".format(text / 100.0)
            build(textFormatted, params)
        })
    }
}