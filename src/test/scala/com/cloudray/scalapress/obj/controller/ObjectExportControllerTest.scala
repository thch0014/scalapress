package com.cloudray.scalapress.obj.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.controller.admin.ObjectExportController
import com.cloudray.scalapress.obj.ObjectExporter
import javax.servlet.http.HttpServletResponse
import org.mockito.Mockito
import java.io.File

/** @author Stephen Samuel */
class ObjectExportControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new ObjectExportController
    controller.exporter = mock[ObjectExporter]
    val resp = mock[HttpServletResponse]

    val file = File.createTempFile("export_test", "csv")
    file.deleteOnExit()
    Mockito.when(controller.exporter.export(14)).thenReturn(file)

    test("controller sets excel header") {
        controller.export(14, resp)
        Mockito.verify(resp).setContentType("application/vnd.ms-excel")
    }

    test("controller sets disposition header") {
        controller.export(14, resp)
        Mockito.verify(resp).setHeader("Content-Disposition", "attachment; filename=export_objects_14.csv")
    }

}
