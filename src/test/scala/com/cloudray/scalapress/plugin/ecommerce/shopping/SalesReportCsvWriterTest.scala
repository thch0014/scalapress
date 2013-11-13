package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.csvreader.CsvReader
import java.nio.charset.Charset
import java.io.ByteArrayInputStream
import com.cloudray.scalapress.plugin.ecommerce.reports.{SalesReportCsvWriter, ReportLine}

/** @author Stephen Samuel */

class SalesReportCsvWriterTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val line1 = ReportLine("abc",
    1114414414100l,
    "sammy",
    "sammy@snake.com",
    "completed",
    10,
    2,
    12,
    Some("wicked order"),
    Seq("captain kirk doll", "mr T van"))
  val line2 = ReportLine("xyz",
    1234567890000l,
    "tony",
    "tony@sopranos.com",
    "rejected",
    50,
    5,
    55,
    None,
    Nil)
  val lines = Seq(line1, line2)

  val report = SalesReportCsvWriter.export(lines)
  val csv = new CsvReader(new ByteArrayInputStream(report), ',', Charset.forName("UTF8"))
  csv.readHeaders()

  "a sales csv writer" should "write all headers" in {
    val headers = csv.getHeaders
    assert("orderId" === headers(0))
    assert("date" === headers(1))
    assert("name" === headers(2))
    assert("email" === headers(3))
    assert("status" === headers(4))
    assert("subtotal" === headers(5))
    assert("vat" === headers(6))
    assert("total" === headers(7))
    assert("customer note" === headers(8))
    assert("details" === headers(9))
  }

  it should "put data in correct columns" in {
    csv.readRecord()
    assert("abc" === csv.get(0))
    assert("25-04-2005" === csv.get(1))
    assert("sammy" === csv.get(2))
    assert("sammy@snake.com" === csv.get(3))
    assert("completed" === csv.get(4))
    assert("10.0" === csv.get(5))
    assert("2.0" === csv.get(6))
    assert("12.0" === csv.get(7))
    assert("wicked order" === csv.get(8))
    assert("captain kirk doll,mr T van" === csv.get(9))

  }
}

