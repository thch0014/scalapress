package com.cloudray.scalapress.item.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.controller.admin.ItemExportController
import com.cloudray.scalapress.item.ItemExporter
import javax.servlet.http.HttpServletResponse
import org.mockito.Mockito
import java.io.File

/** @author Stephen Samuel */
class ObjectExportControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val exporter = mock[ItemExporter]
  val resp = mock[HttpServletResponse]
  val controller = new ItemExportController(exporter)

  val file = File.createTempFile("export_test", "csv")
  file.deleteOnExit()
  Mockito.when(exporter.export(14)).thenReturn(file)

  test("controller sets excel header") {
    controller.export(14, resp)
    Mockito.verify(resp).setContentType("application/vnd.ms-excel")
  }

  test("controller sets disposition header") {
    controller.export(14, resp)
    Mockito.verify(resp).setHeader("Content-Disposition", "attachment; filename=export_objects_14.csv")
  }

}
