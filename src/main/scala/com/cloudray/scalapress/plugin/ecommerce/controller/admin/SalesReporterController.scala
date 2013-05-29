package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.ecommerce.SalesReporter
import com.cloudray.scalapress.ScalapressContext
import org.joda.time.{DateTime, DateTimeZone, DateMidnight}
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import scala.collection.mutable
import org.joda.time.format.DateTimeFormat

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/shopping/salesreport"))
class SalesReporterController {

    @Autowired var reporter: SalesReporter = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def invoice(model: ModelMap,
                @RequestParam(value = "from", required = false) from: String,
                @RequestParam(value = "to", required = false) to: String): String = {

        val parser = DateTimeFormat.forPattern("MMMMM yyyy").withZone(DateTimeZone.UTC)

        val fromDate = Option(from)
          .map(parser.parseDateTime(_))
          .getOrElse(new DateTime(DateTimeZone.UTC))
          .withDayOfMonth(1)
          .withMillisOfDay(0)

        val toDate = Option(to)
          .map(parser.parseDateTime(_))
          .getOrElse(new DateTime(DateTimeZone.UTC))
          .dayOfMonth().withMaximumValue()
          .millisOfDay.withMaximumValue

        val lines = reporter.generate(fromDate.getMillis, toDate.getMillis)
        model.put("lines", lines.asJava)

        val total = lines.map(_.total).sum
        val vat = lines.map(_.vat).sum
        val vatableSalesTotal = lines.filter(_.vat > 0).map(_.subtotal).sum
        val vatFreeSalesTotal = lines.filter(_.vat < 0.01).map(_.subtotal).sum

        model.put("vatableSalesTotal", vatableSalesTotal.toString)
        model.put("vatFreeSalesTotal", vatFreeSalesTotal.toString)
        model.put("vat", vat.toString)
        model.put("total", total.toString)

        model.addAttribute("from", fromDate.toString("MMMMM yyyy"))
        model.addAttribute("to", toDate.toString("MMMMM yyyy")

        "admin/plugin/shopping/salesreport.vm"
    }

    @ModelAttribute("months") def months: java.util.Map[String, String] = {
        val map = mutable.LinkedHashMap[String, String]()
        val now = new DateMidnight(DateTimeZone.UTC).withDayOfMonth(1)
        for ( i <- 0 to 36 )
            map.put(now.minusMonths(i).toString("MMMMM yyyy"), now.minusMonths(i).toString("MMMMM yyyy"))
        map.asJava
    }
}
