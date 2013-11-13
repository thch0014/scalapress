package com.cloudray.scalapress.plugin.ecommerce.shopping

import java.io.ByteArrayOutputStream
import com.csvreader.CsvWriter
import scala.collection.mutable.{ListBuffer, ArrayBuffer}
import org.joda.time.{DateTimeZone, DateTime}
import java.nio.charset.Charset

/** @author Stephen Samuel */
object SalesReportCsvWriter {

  def export(lines: Seq[ReportLine]): Array[Byte] = {
    val out = new ByteArrayOutputStream()
    val csv = new CsvWriter(out, ',', Charset.forName("UTF-8"))
    csv.writeRecord(_headers)
    for ( line <- lines ) {
      csv.writeRecord(_row(line))
    }
    csv.close()
    out.toByteArray
  }

  def _headers: Array[String] = {
    val buffer = new ArrayBuffer[String]
    buffer.append("orderId")
    buffer.append("date")
    buffer.append("name")
    buffer.append("email")
    buffer.append("status")
    buffer.append("subtotal")
    buffer.append("vat")
    buffer.append("total")
    buffer.append("customer note")
    buffer.append("details")
    buffer.toArray
  }

  def _row(line: ReportLine): Array[String] = {
    val buffer = new ListBuffer[String]
    buffer.append(line.orderId)
    buffer.append(new DateTime(line.datePlaced, DateTimeZone.UTC).toString("dd-MM-yyyy"))
    buffer.append(line.name)
    buffer.append(line.email)
    buffer.append(line.status)
    buffer.append(line.subtotal.toString)
    buffer.append(line.vat.toString)
    buffer.append(line.total.toString)
    buffer.append(line.note.orNull)
    buffer.append(line.details.mkString(","))
    buffer.toArray
  }
}
