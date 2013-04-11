package com.liferay.scalapress.obj.controller.admin

import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.obj.ObjectExporter
import org.apache.commons.io.FileUtils
import java.io.InputStream

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type/{id}/export"))
class ObjectExportController {

    @Autowired var exporter: ObjectExporter = _

    @RequestMapping(produces = Array("text/csv"), value = Array("csv"))
    @ResponseBody
    def export(@PathVariable("id") id: Long, resp: HttpServletResponse): InputStream = {
        resp.setHeader("Content-Disposition", "attachment; filename=export_objects_" + id + ".csv")
        val file = exporter.export(id)
        FileUtils.openInputStream(file)
    }
}
