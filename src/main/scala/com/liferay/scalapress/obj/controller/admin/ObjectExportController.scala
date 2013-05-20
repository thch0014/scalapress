package com.liferay.scalapress.obj.controller.admin

import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.obj.ObjectExporter
import org.apache.commons.io.{IOUtils, FileUtils}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type/{id}/export"))
class ObjectExportController {

    @Autowired var exporter: ObjectExporter = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"), value = Array("csv"))
    def export(@PathVariable("id") id: Long, resp: HttpServletResponse) {
        resp.setHeader("Content-Disposition", "attachment; filename=export_objects_" + id + ".csv")
        val file = exporter.export(id)
        val input = FileUtils.openInputStream(file)
        IOUtils.copy(input, resp.getOutputStream)
        IOUtils.closeQuietly(input)
    }
}
