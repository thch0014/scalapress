package com.cloudray.scalapress.plugin.ecommerce

import java.io.{FileWriter, BufferedWriter, File}
import com.csvreader.CsvWriter
import scala.collection.mutable.ArrayBuffer
import org.joda.time.{DateTimeZone, DateTime}

/** @author Stephen Samuel */
object SalesReportCsvWriter {

    def export(lines: Seq[ReportLine]): File = {

        val file = File.createTempFile("sales_report", ".csv")
        file.deleteOnExit()

        val writer = new BufferedWriter(new FileWriter(file))
        val csv = new CsvWriter(writer, ',')
        csv.writeRecord(_header)

        for ( line <- lines ) {
            csv.writeRecord(_row(line))
        }

        writer.close()
        file
    }

    def _header: Array[String] = {
        val buffer = new ArrayBuffer[String]
        buffer.append("orderId")
        buffer.append("date")
        buffer.append("status")
        buffer.append("subtotal")
        buffer.append("vat")
        buffer.append("total")
        buffer.toArray
    }

    def _row(line: ReportLine): Array[String] = {
        val buffer = new ArrayBuffer[String]
        buffer.append(line.orderId)
        buffer.append(new DateTime(line.datePlaced, DateTimeZone.UTC).toString("dd-MM-yyyy"))
        buffer.append(line.status)
        buffer.append(line.subtotal.toString)
        buffer.append(line.vat.toString)
        buffer.append(line.total.toString)
        buffer.toArray
    }
}