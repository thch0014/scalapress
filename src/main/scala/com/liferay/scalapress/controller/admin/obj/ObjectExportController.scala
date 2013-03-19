package com.liferay.scalapress.controller.admin.obj

import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.stereotype.Controller
import com.csvreader.CsvWriter
import java.io.StringWriter
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.service.FriendlyUrlGenerator
import javax.servlet.http.HttpServletResponse
import com.liferay.scalapress.obj.{ObjectDao, TypeDao, ObjectType, Obj}
import com.liferay.scalapress.obj.attr.Attribute

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type/{id}/export"))
class ObjectExportController {

    @Autowired var objectTypeDao: TypeDao = _
    @Autowired var objectDao: ObjectDao = _

    @RequestMapping(produces = Array("text/csv"), value = Array("csv"))
    @ResponseBody
    def export(@PathVariable("id") id: Long, resp: HttpServletResponse) = {

        resp.setHeader("Content-Disposition", "attachment; filename=export_objects_" + id + ".csv")

        val objectType = objectTypeDao.find(id)
        val attributes = objectType.attributes.asScala.toSeq.sortBy(_.name)
        val objects = objectDao.findByType(id)

        val writer = new StringWriter
        val csv = new CsvWriter(writer, ',')
        csv.writeRecord(_header(objectType, attributes))

        for (obj <- objects) {
            csv.writeRecord(_row(obj, attributes))
        }

        writer.toString
    }

    private def _header(objectType: ObjectType, attributes: Seq[Attribute]): Array[String] = {
        val buffer = new ArrayBuffer[String]
        buffer.append("id")
        buffer.append("name")
        buffer.append("url")
        for (attribute <- attributes) {
            buffer.append(attribute.name)
        }
        buffer.toArray
    }

    private def _row(obj: Obj, attributes: Seq[Attribute]): Array[String] = {
        val buffer = new ArrayBuffer[String]
        buffer.append(obj.id.toString)
        buffer.append(obj.name)
        buffer.append(FriendlyUrlGenerator.friendlyUrl(obj))
        val values = obj.attributeValues.asScala
        for (attribute <- attributes) {
            val value = values.find(av => av.attribute.id == attribute.id).map(_.value).orNull
            buffer.append(value)
        }
        buffer.toArray
    }
}
