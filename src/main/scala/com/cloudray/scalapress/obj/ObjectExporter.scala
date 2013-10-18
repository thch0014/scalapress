package com.cloudray.scalapress.obj

import com.cloudray.scalapress.obj.attr.{AttributeFuncs, Attribute}
import scala.collection.mutable.ArrayBuffer
import org.joda.time.{DateTimeZone, DateTime}
import scala.collection.JavaConverters._
import java.io.{BufferedWriter, FileWriter, File}
import com.csvreader.CsvWriter
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.settings.InstallationDao
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
@Component
class ObjectExporter {

  @Autowired var objectTypeDao: TypeDao = _
  @Autowired var objectDao: ObjectDao = _
  @Autowired var installationDao: InstallationDao = _

  def export(objectTypeId: Long): File = {

    val domain = installationDao.get.domain
    val objectType = objectTypeDao.find(objectTypeId)
    val attributes = objectType.attributes.asScala.toSeq.sortBy(_.name)

    val q = new ObjectQuery
    q.typeId = Some(objectTypeId)
    q.status = Some(Obj.STATUS_LIVE)
    q.pageSize = Integer.MAX_VALUE
    val objects = objectDao.search(q).results

    val file = File.createTempFile("object_export", ".csv")
    file.deleteOnExit()

    val writer = new BufferedWriter(new FileWriter(file))
    val csv = new CsvWriter(writer, ',')
    csv.writeRecord(_header(attributes))

    for ( obj <- objects ) {
      csv.writeRecord(_row(obj, attributes, domain))
    }

    writer.close()
    file
  }

  def _header(attributes: Seq[Attribute]): Array[String] = {
    val buffer = new ArrayBuffer[String]
    buffer.append("id")
    buffer.append("date")
    buffer.append("name")
    buffer.append("status")
    buffer.append("url")
    buffer.append("price")
    buffer.append("vat rate")
    buffer.append("price inc")
    buffer.append("rrp")
    buffer.append("cost")
    buffer.append("profit")
    buffer.append("stock")
    for ( attribute <- attributes ) {
      buffer.append(attribute.name)
    }
    buffer.toArray
  }

  def _row(obj: Obj, attributes: Seq[Attribute], domain: String): Array[String] = {

    val buffer = new ArrayBuffer[String]

    buffer.append(obj.id.toString)
    buffer.append(new DateTime(obj.dateCreated, DateTimeZone.UTC).toString("dd-MM-yyyy"))
    buffer.append(obj.name)
    buffer.append(obj.status)
    buffer.append("http://" + domain + UrlGenerator.url(obj))

    val price = "%1.2f".format(obj.price / 100.0)
    val priceInc = "%1.2f".format(obj.sellPriceInc / 100.0)
    val cost = "%1.2f".format(obj.costPrice / 100.0)
    val rrp = "%1.2f".format(obj.rrp / 100.0)
    val profit = "%1.2f".format(obj.profit / 100.0)

    buffer.append(price)
    buffer.append(obj.vatRate.toString)
    buffer.append(priceInc)
    buffer.append(rrp)
    buffer.append(cost)
    buffer.append(profit)
    buffer.append(obj.stock.toString)

    for ( attribute <- attributes ) {
      val values = AttributeFuncs.attributeValues(obj, attribute).mkString("|")
      buffer.append(values)
    }
    buffer.toArray
  }
}
