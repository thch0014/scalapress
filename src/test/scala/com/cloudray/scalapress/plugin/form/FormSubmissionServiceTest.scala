package com.cloudray.scalapress.plugin.form

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class FormSubmissionServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  val assetStore = mock[AssetStore]
  val mailSender = mock[MailSender]
  val submissionDao = mock[SubmissionDao]

  val service = new FormSubmissionService(context, assetStore, submissionDao, mailSender)

  val req = mock[HttpServletRequest]
  context.installationDao = mock[InstallationDao]

  val installation = new Installation
  installation.name = "big man shop"
  installation.domain = "coldplay.com"

  Mockito.when(req.getRemoteAddr).thenReturn("56.78.45.3")
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val sreq = ScalapressRequest(req, context)

  val form = new Form
  form.name = "contact us"

  val field1 = new FormField
  field1.name = "name"
  field1.id = 5

  val field2 = new FormField
  field2.name = "email"
  field2.id = 8

  form.fields.add(field1)
  form.fields.add(field2)


  Mockito.when(req.getParameter("5")).thenReturn("sammy")
  Mockito.when(req.getParameter("8")).thenReturn("s@sam.com")
  Mockito.when(req.getParameterValues("5")).thenReturn(Array("sammy"))
  Mockito.when(req.getParameterValues("8")).thenReturn(Array("s@sam.com"))

  test("creating submission sets date") {
    val lower = new DateTime(DateTimeZone.UTC).minusMinutes(1).getMillis
    val upper = new DateTime(DateTimeZone.UTC).plusMinutes(1).getMillis
    val sub = service._createSubmission(form, Nil, sreq)
    assert(lower <= sub.date)
    assert(upper >= sub.date)
  }

  test("creating submission sets ip from sreq") {
    val sub = service._createSubmission(form, Nil, sreq)
    assert("56.78.45.3" === sub.ipAddress)
  }

  test("creating submission sets formName from form") {
    val sub = service._createSubmission(form, Nil, sreq)
    assert("contact us" === sub.formName)
  }

  test("creating submission sets data from form parameters") {
    val sub = service._createSubmission(form, Nil, sreq)
    assert("sammy" === sub.data.asScala.find(_.key == "name").get.value)
    assert("s@sam.com" === sub.data.asScala.find(_.key == "email").get.value)
  }

  test("creating submission sets attachments from assets") {
    val assets = Seq("coldplay.png", "elo.png")
    val sub = service._createSubmission(form, assets, sreq)
    assert(Set("coldplay.png", "elo.png").asJava === sub.attachments)
  }

  test("submitting a form sends email to user using the submitterEmailField") {
    field2.submitterEmailField = true
    form.submissionEmailBody = "thanks for the form dude"
    service.doSubmission(form, sreq, Nil)

    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getTo === Array("s@sam.com"))
    assert(msg.getFrom === "donotreply@coldplay.com")
  }

  test("no submitter email is sent if no submitter field is set") {
    form.submissionEmailBody = "thanks for the form dude"
    Mockito.verify(mailSender, Mockito.never).send(Matchers.any[SimpleMailMessage])
  }

  test("submitting a form sends email to admin recipients") {
    form.recipients = "admin@sambo.com,rambo@first.com"
    service.doSubmission(form, sreq, Nil)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    val msg = captor.getValue
    assert(msg.getTo.contains("admin@sambo.com"))
    assert(msg.getTo.contains("rambo@first.com"))
    assert(msg.getSubject.contains("contact us"))
    assert(msg.getFrom === "donotreply@coldplay.com")
  }

  test("no admin email is sent if no recipients are set") {
    form.recipients = ""
    service.doSubmission(form, sreq, Nil)
    Mockito.verify(mailSender, Mockito.never).send(Matchers.any[SimpleMailMessage])
  }

  test("submitting a form persists submission") {
    val sub = service.doSubmission(form, sreq, Nil)
    Mockito.verify(submissionDao).save(sub)
  }

  test("a field that matches a regex does not cause an error") {
    field2.regExp = "\\d{4}" // need 4 digits
    Mockito.when(req.getParameter("8")).thenReturn("1234")
    Mockito.when(req.getParameterValues("8")).thenReturn(Array("1234"))

    assert(!sreq.hasErrors)
    service.checkErrors(form, sreq)
    println(sreq.errors)
    assert(!sreq.hasErrors)
  }

  test("a field that does not match a regex causes an error") {
    field2.regExp = "\\d{4}" // need 4 digits
    Mockito.when(req.getParameter("8")).thenReturn("12345")
    Mockito.when(req.getParameterValues("8")).thenReturn(Array("12345"))

    assert(!sreq.hasErrors)
    service.checkErrors(form, sreq)
    assert(sreq.hasErrors)
  }

  test("a required field that is missing causes an error") {
    field2.required = true
    Mockito.when(req.getParameter("8")).thenReturn(null)
    Mockito.when(req.getParameterValues("8")).thenReturn(null)
    assert(!sreq.hasErrors)
    service.checkErrors(form, sreq)
    assert(sreq.hasErrors)
  }

}
