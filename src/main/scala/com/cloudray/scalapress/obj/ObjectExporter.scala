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
        val objects = objectDao.findByType(objectTypeId)

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
        buffer.append("url")
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
        buffer.append("http://" + domain + UrlGenerator.url(obj))
        for ( attribute <- attributes ) {
            val value = AttributeFuncs.attributeValue(obj, attribute).orNull
            buffer.append(value)
        }
        buffer.toArray
    }
}
