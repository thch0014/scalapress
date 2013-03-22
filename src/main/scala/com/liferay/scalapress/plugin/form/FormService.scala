package com.liferay.scalapress.plugin.form

import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.{Logging, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import org.springframework.mail.{MailSender, SimpleMailMessage}
import com.liferay.scalapress.enums.FormFieldType
import org.apache.commons.lang.StringUtils
import com.liferay.scalapress.settings.Installation
import com.liferay.scalapress.media.AssetStore

/** @author Stephen Samuel */
@Component
class FormService extends Logging {

    @Autowired var assetStore: AssetStore = _
    @Autowired var submissionDao: SubmissionDao = _

    @Autowired
    var mailSender: MailSender = _

    def doSubmission(form: Form, req: ScalapressRequest, files: Seq[MultipartFile]) = {

        // upload files first
        val keys = files.map(file => assetStore.add(file.getOriginalFilename, file.getInputStream))
        logger.debug("Saved attachments [{}]", keys)

        val submission = new Submission
        submission.attachments = keys.asJava
        submission.formName = form.name
        submission.date = System.currentTimeMillis()
        submission.ipAddress = req.request.getRemoteAddr
        submission.folder = req.folder.orNull
        submission.obj = req.obj.orNull
        submission.data = form
          .fields
          .asScala
          .filter(field => Option(req.request.getParameter(field.id.toString)).isDefined)
          .map(field => {
            val kv = new SubmissionKeyValue
            kv.submission = submission
            kv.key = field.name
            kv.value = req.request.getParameterValues(field.id.toString).mkString(", ")
            kv
        }).toList.asJava

        submissionDao.save(submission)
        submission
    }

    def email(recipients: Seq[String], submission: Submission, installation: Installation) {

        val body = new StringBuilder
        for (kv <- submission.data.asScala) {
            body.append(kv.key + ": " + kv.value + "\n")
        }

        val message = new SimpleMailMessage()

        val nowww = Option(installation.domain) match {
            case Some(domain) if domain.startsWith("www") => domain.drop(4)
            case Some(domain) => domain
            case _ => "nodomain.com"
        }

        message.setFrom("nodotreply@" + nowww)
        message.setTo(recipients.toArray)
        message.setSubject("Submission: " + submission.formName)
        message.setText(body.toString())

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }

    def checkErrors(form: Form, sreq: ScalapressRequest) {
        val checkedFields = form.fields.asScala
          .filterNot(_.fieldType == FormFieldType.Attachment)
          .filterNot(_.fieldType == FormFieldType.Header)
          .filterNot(_.fieldType == FormFieldType.TickBox)
          .filterNot(_.fieldType == FormFieldType.Description)

        for (field <- checkedFields if field.required || StringUtils.isNotBlank(field.regExp)) {

            val regExp =
                if (field.fieldType == FormFieldType.Email) "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
                else field.regExp

            val value = Option(sreq.request.getParameter(field.id.toString)).filter(_.trim.length > 0)
            value match {
                case Some(v) if regExp == null =>
                case Some(v) if v.matches(regExp) =>
                case _ => sreq.error(field.id.toString, "This is a required field")
            }
        }
    }
}
