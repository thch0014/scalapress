package com.liferay.scalapress.plugin.form

import javax.servlet.http.HttpServletRequest
import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.{Logging, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.asset.AssetStore
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.liferay.scalapress.dao.SubmissionDao
import org.springframework.mail.{MailSender, SimpleMailMessage}
import com.liferay.scalapress.enums.FormFieldType

/** @author Stephen Samuel */
@Component
class FormService extends Logging {

    @Autowired var assetStore: AssetStore = _
    @Autowired var submissionDao: SubmissionDao = _

    @Autowired
    var mailSender: MailSender = _

    def doSubmission(form: Form, req: HttpServletRequest, files: Seq[MultipartFile]) = {

        // upload files first
        val keys = files.map(file => assetStore.add(file.getInputStream))
        logger.debug("Saved attachments [{}]", keys)

        val submission = new Submission
        submission.attachments = keys.asJava
        submission.formName = form.name
        submission.date = System.currentTimeMillis()
        submission.ipAddress = req.getRemoteAddr
        submission.data = form
          .fields
          .asScala
          .filter(field => Option(req.getParameter(field.id.toString)).isDefined)
          .map(field => {
            val kv = new SubmissionKeyValue
            kv.submission = submission
            kv.key = field.name
            kv.value = req.getParameterValues(field.id.toString).mkString(", ")
            kv
        }).toList.asJava

        submissionDao.save(submission)
        submission
    }

    def email(recipients: Seq[String], submission: Submission) {

        val body = new StringBuilder
        for (kv <- submission.data.asScala) {
            body.append(kv.key + ": " + kv.value + "\n")
        }

        val message = new SimpleMailMessage()

        message.setFrom("sam@sksamuel.com")
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
        for (field <- form.fields.asScala) {
            if (field.required || field.regExp != null) {

                val regExp =
                    if (field.fieldType == FormFieldType.Email) "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"
                    else field.regExp

                val value = Option(sreq.request.getParameter(field.id.toString)).filter(_.trim.length > 0)

                value match {
                    case Some(v) if (regExp == null) =>
                    case Some(v) if (v.matches(regExp)) =>
                    case _ => sreq.error(field.id.toString, "This is a required field")
                }
            }
        }
    }
}
