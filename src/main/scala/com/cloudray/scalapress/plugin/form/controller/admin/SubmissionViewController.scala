package com.cloudray.scalapress.plugin.form.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, ModelAttribute, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import java.util.zip.{ZipEntry, ZipOutputStream}
import com.cloudray.scalapress.plugin.form.{SubmissionDao, Submission}
import scala.collection.JavaConverters._
import java.io.ByteArrayOutputStream
import org.apache.commons.io.IOUtils
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/submission/{id}"))
class SubmissionViewController(context: ScalapressContext,
                               submissionDao: SubmissionDao) {

  @RequestMapping(produces = Array("text/html"))
  def view = "admin/plugin/form/submissions/view.vm"

  @RequestMapping(produces = Array("text/html"), value = Array("downloadall"))
  @ResponseBody
  def downloadAll(@ModelAttribute("submission") submission: Submission, resp: HttpServletResponse): Array[Byte] = {
    resp.setHeader("Content-Disposition", "attachment; filename=attachments_submission_" + submission.id + ".zip")

    val bos = new ByteArrayOutputStream
    val zip = new ZipOutputStream(bos)
    submission.attachments.asScala.foreach(a => {

      context.assetStore.get(a) match {
        case Some(asset) =>
          val e = new ZipEntry(a)
          zip.putNextEntry(e)
          IOUtils.copy(asset, zip)
          zip.closeEntry()
        case _ =>
      }
    })

    zip.close()
    bos.toByteArray
  }

  @ModelAttribute("assetStore") def assetStore = context.assetStore
  @ModelAttribute("submission") def submission(@PathVariable("id") id: Long) = submissionDao.find(id)
}
