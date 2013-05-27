package com.liferay.scalapress.plugin.form

import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.{ScalapressContext, Logging, ScalapressRequest}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import org.springframework.mail.{MailSender, SimpleMailMessage}
import com.liferay.scalapress.enums.FormFieldType
import com.liferay.scalapress.media.AssetStore
import org.joda.time.{DateTimeZone, DateTime}
import scala.collection.mutable.ListBuffer

/** @author Stephen Samuel */
@Component
class FormService extends Logging {

    val EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"

    @Autowired var context: ScalapressContext = _
    @Autowired var assetStore: AssetStore = _
    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var mailSender: MailSender = _

    def doSubmission(form: Form, sreq: ScalapressRequest, files: Iterable[MultipartFile]): Submission = {

        val assets = _uploadMedia(files)
        val submission = _createSubmission(form, assets, sreq)
        _adminEmail(submission, form)
        _submitterEmail(submission, form)
        submission
    }

    def _uploadMedia(files: Iterable[MultipartFile]): Iterable[String] = {
        val assetKeys = files.map(file => assetStore.add(file.getOriginalFilename, file.getInputStream))
        logger.debug("Saved attachments [{}]", assetKeys)
        assetKeys
    }

    def _createSubmission(form: Form, assets: Iterable[String], sreq: ScalapressRequest): Submission = {

        val submission = new Submission
        submission.attachments = assets.toSet.asJava
        submission.formName = form.name
        submission.date = new DateTime(DateTimeZone.UTC).getMillis

        submission.ipAddress = sreq.request.getRemoteAddr
        submission.folder = sreq.folder.orNull
        submission.obj = sreq.obj.orNull

        submission.data = form.fields.asScala
          .filter(field => Option(sreq.request.getParameter(field.id.toString)).isDefined)
          .map(field => {
            val kv = new SubmissionKeyValue
            kv.submission = submission
            kv.key = field.name
            kv.value = sreq.request.getParameterValues(field.id.toString).mkString(", ")
            kv
        }).toList.asJava

        submissionDao.save(submission)
        submission
    }

    def _domain =
        Option(context.installationDao.get.domain) match {
            case Some(domain) if domain.startsWith("www") => domain.drop(4)
            case Some(domain) => domain
            case _ => "nodomain.com"
        }

    def _submitterEmail(submission: Submission, form: Form) {

        Option(form.submissionEmailBody) match {
            case Some(body) =>
                form.submissionField match {
                    case Some(field) =>
                        submission.data.asScala.find(_.key == field.name) match {
                            case Some(kv) =>

                                val subject = Option(form.submissionEmailSubject).getOrElse("Form submission received")

                                val message = new SimpleMailMessage()
                                message.setFrom("nodotreply@" + _domain)
                                message.setTo(kv.value)
                                message.setSubject(subject)
                                message.setText(body)

                                try {
                                    mailSender.send(message)
                                } catch {
                                    case e: Exception => logger.warn(e.toString)
                                }
                            case _ =>
                        }
                    case _ =>
                }
            case _ =>
        }
    }

    def _adminEmail(submission: Submission, form: Form) {

        val recipients = Option(form.recipients)
          .map(_.split(Array(' ', '\n', ',')).map(_.trim).filter(_.length > 0))
          .getOrElse(Array[String]())

        if (recipients.size > 0) {

            val body = new ListBuffer[String]
            body.append("Form name: " + submission.formName)
            submission.page.foreach(page => body.append("Submitted on page: " + page.name))
            for ( kv <- submission.data.asScala ) {
                body.append(kv.key + ": " + kv.value)
            }

            val message = new SimpleMailMessage()
            message.setFrom("nodotreply@" + _domain)
            message.setTo(recipients.toArray)
            message.setSubject("Submission: " + submission.formName)
            message.setText(body.mkString("\n"))

            try {
                mailSender.send(message)
            } catch {
                case e: Exception => logger.warn(e.toString)
            }
        }
    }

    def checkErrors(form: Form, sreq: ScalapressRequest) {

        val checkedFields = form.fields.asScala
          .filterNot(_.fieldType == FormFieldType.Attachment)
          .filterNot(_.fieldType == FormFieldType.Header)
          .filterNot(_.fieldType == FormFieldType.TickBox)
          .filterNot(_.fieldType == FormFieldType.Description)

        for ( field <- checkedFields ) {

            val default = if (field.fieldType == FormFieldType.Email) Some(EMAIL_REGEX) else None
            val regex = Option(field.regExp).orElse(default)

            Option(sreq.request.getParameter(field.id.toString)).filterNot(_.isEmpty) match {
                case Some(value) =>
                    regex match {
                        case Some(r) if !value.matches(r) => sreq.error(field.id.toString, "Invalid entry")
                        case _ =>
                    }
                case None if field.required => sreq.error(field.id.toString, "This is a required field")
                case _ =>
            }
        }
    }
}
