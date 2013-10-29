package com.cloudray.scalapress.item.controller.admin

import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.item.ItemExporter
import org.apache.commons.io.{IOUtils, FileUtils}
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("/backoffice/type/{id}/export"))
class ItemExportController(exporter: ItemExporter) {

  def filename(id: Any) = s"export_items_$id.csv"

  @ResponseBody
  @RequestMapping(produces = Array("text/html"), value = Array("csv"))
  def export(@PathVariable("id") id: Long, resp: HttpServletResponse) {
    resp.setContentType("application/vnd.ms-excel")
    resp.setHeader("Content-Disposition", "attachment; filename=" + filename(id))
    val file = exporter.export(id)
    val input = FileUtils.openInputStream(file)
    IOUtils.copy(input, resp.getOutputStream)
    IOUtils.closeQuietly(input)
  }
}
