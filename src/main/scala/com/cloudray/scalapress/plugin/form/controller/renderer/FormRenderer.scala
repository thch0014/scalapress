package com.cloudray.scalapress.plugin.form.controller.renderer

import scala.collection.JavaConverters._
import xml.{Node, Unparsed}
import com.cloudray.scalapress.plugin.form.{FieldSize, FormFieldType, FormField, Form}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
object FormRenderer {

  def action(form: Form, folderId: Long, objectId: Long) = s"/form/${form.id}?folderId=$folderId&objId=$objectId"
  def action(form: Form, folder: Option[Folder], obj: Option[Item]): String =
    action(form, folder.map(_.id).getOrElse(0l), obj.map(_.id).getOrElse(0l))
  def action(form: Form, req: ScalapressRequest): String = action(form, req.folder, req.item)

  def render(form: Form, req: ScalapressRequest): String = {
    val fields = form.fields.asScala.sortBy(_.position)
    val renderedFields = fields.map(renderField(_, req))
    val captchaError = req.hasError("captcha.error")

    <form method="POST" enctype="multipart/form-data" action={action(form, req)} class="form-horizontal">
      {if (captchaError) <div class='alert alert-error'>Please try the captcha again</div> else null}{renderedFields}{if (form
      .captcha) Unparsed(_captcha)
    else ""}{Unparsed(button(form))}
    </form>.toString()
  }

  def button(form: Form) = Scalate.layout(
    "/com/cloudray/scalapress/plugin/form/controller/renderer/button.ssp",
    Map("text" -> Option(form.submitButtonText).filterNot(_.isEmpty).getOrElse("Submit"))
  )

  def _captcha = Scalate.layout("/com/cloudray/scalapress/plugin/form/controller/renderer/captcha.ssp")

  private def renderField(field: FormField, req: ScalapressRequest): Node = {

    val name = field.name + (if (field.required) "*" else "")
    val value = Option(req.request.getParameter(field.id.toString))
    val error = req.error(field.id.toString)

    field.fieldType match {
      case FormFieldType.DropDownMenu => _renderSelect(field, req)
      case FormFieldType.TickBox => Unparsed(_renderCheck(field))
      case FormFieldType.TickBoxes => renderChecks(field, req)
      case FormFieldType.Radio => renderRadio(field, req)
      case FormFieldType.Description =>
        <p>
          {Unparsed(field.name)}
        </p>
      case FormFieldType.Header =>
        <legend>
          {scala.xml.Unparsed(field.name)}
        </legend>
      case FormFieldType.Attachment => Unparsed(_renderUpload(field))
      case FormFieldType.TextArea => Unparsed(_renderTextArea(field, req))
      case _ => renderText(field, name, value, error, req)
    }
  }

  private def _renderTextArea(field: FormField, req: ScalapressRequest): String = {
    val star = if (field.required) "*" else ""
    val text = Option(req.request.getParameter(field.id.toString)).getOrElse("")
    Scalate.layout(
      "/com/cloudray/scalapress/plugin/form/controller/renderer/textarea.ssp",
      Map("id" -> field.id.toString, "name" -> field.name, "text" -> text, "star" -> star))
  }

  private def _renderUpload(field: FormField) = {
    val star = if (field.required) "*" else ""
    Scalate.layout(
      "/com/cloudray/scalapress/plugin/form/controller/renderer/upload.ssp",
      Map("name" -> field.name, "star" -> star))
  }

  private def renderText(field: FormField,
                         name: String,
                         value: Option[String],
                         error: Option[String],
                         req: ScalapressRequest) = {

    val cssClass = "control-group" + (if (error.isDefined) " error" else "")
    val css = Option(field.size).getOrElse(Some(FieldSize.Medium)) match {
      case FieldSize.Small => "input-small"
      case FieldSize.Large => "input-large"
      case FieldSize.XLarge => "input-xlarge"
      case FieldSize.XXLarge => "input-xxlarge"
      case _ => "input-medium"
    }

    <div class={cssClass}>
      <label class="control-label">
        {Unparsed(name)}
      </label>
      <div class="controls">
        <input type="text" name={field.id.toString} placeholder={field.placeholder} value={value
        .orNull} class={css}/>
        <span class="help-inline">
          {error.orNull}
        </span>
      </div>
    </div>
  }

  def _renderOptions(options: Array[String]): Seq[Node] = {
    options.map(opt =>
      scala.xml.Utility.trim(<option>
        {opt}
      </option>))
  }

  def _renderSelect(field: FormField, req: ScalapressRequest) = {
    val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
    val star = if (field.required) "*" else ""

    val opts = _renderOptions(field.optionsList)

    scala.xml.Utility.trim(<div class={cssClass}>
      <label class="control-label">
        {Unparsed(field.name)}{star}
      </label>
      <div class="controls">
        <select name={field.id.toString}>
          {opts}
        </select>
        <span class="help-inline">
          {req.errors.getOrElse(field.id.toString, "")}
        </span>
      </div>
    </div>)
  }

  def _renderCheck(field: FormField) = Scalate.layout(
    "/com/cloudray/scalapress/plugin/form/controller/renderer/check.ssp",
    Map("id" -> field.id.toString, "name" -> field.name))

  private def renderChecks(field: FormField, req: ScalapressRequest) = {
    val checks = field.optionsList.map(opt => {
      val value = Unparsed(opt)
      <label class="checkbox">
        <input type="checkbox" name={field.id.toString} value={value}/>{value}
      </label>
    })

    val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
    <div class={cssClass}>
      <div class="controls">
        <label class="checkbox">
          {Unparsed(field.name)}
        </label>{checks}<span class="help-inline">
        {req.errors.getOrElse(field.id.toString, "")}
      </span>
      </div>
    </div>
  }

  private def renderRadio(field: FormField, req: ScalapressRequest) = {
    val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
    val star = if (field.required) "*" else ""

    val radios = field.optionsList.map(opt =>
      <label class="radio">
        <input type="radio" name={field.id.toString} value={opt}/>{Unparsed(opt)}
      </label>)

    <div class={cssClass}>
      <div class="controls">
        <label class="radio">
          {Unparsed(field.name)}{star}
        </label>{radios}<span class="help-inline">
        {req.errors.getOrElse(field.id.toString, "")}
      </span>
      </div>
    </div>
  }
}