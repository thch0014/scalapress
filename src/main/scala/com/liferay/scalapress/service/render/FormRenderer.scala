package com.liferay.scalapress.service.render

import com.liferay.scalapress.domain.form.{FormField, Form}
import com.liferay.scalapress.domain.{Folder, FieldType}
import scala.collection.JavaConverters._
import xml.Elem
import com.liferay.scalapress.ScalapressRequest

/** @author Stephen Samuel */
object FormRenderer {

    private def action(form: Form, folder: Option[Folder]) = "/form/" + form.id + "?folder=" +
      folder.map(_.id).getOrElse("")

    def render(form: Form, req: ScalapressRequest): String = {
        val fields = form.fields.asScala.sortBy(_.position)
        val renderedFields = fields.map(renderField(_, req))

        <form method="POST" enctype="multipart/form-data" action={action(form, req.folder)} class="form-horizontal">
            {renderedFields}{button(form)}
        </form>.toString()
    }

    def button(form: Form) = {
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">
                    {form.submitButtonText}
                </button>
            </div>
        </div>
    }

    private def renderField(field: FormField, req: ScalapressRequest): Elem = {

        val hasError = req.errors.contains(field.id.toString)
        val errorMsg = req.errors.get(field.id.toString).getOrElse("")

        field.fieldType match {
            case FieldType.DropDownMenu => renderSelect(field, req)
            case FieldType.Text => renderText(field, req)
            case FieldType.TickBox => renderCheck(field)
            case FieldType.TickBoxes => renderChecks(field)
            case FieldType.Radio => renderRadio(field, req)
            case FieldType.Header =>
                <legend>
                    {field.name}
                </legend>
            case FieldType.Attachment => renderUpload(field)
            case FieldType.Email => renderText(field, req)
        }
    }

    private def renderUpload(field: FormField) = {
        val star = if (field.required) "*" else ""
        <div class="control-group">
            <label class="control-label">
                {field.name}{star}
            </label>
            <div class="controls">
                <input type="file" name="file"/>
            </div>
        </div>
    }

    private def renderText(field: FormField, req: ScalapressRequest) = {

        val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
        val star = if (field.required) "*" else ""
        val value = Option(req.request.getParameter(field.id.toString)).getOrElse("")

        <div class={cssClass}>
            <label class="control-label">
                {field.name}{star}
            </label>
            <div class="controls">
                <input type="text" name={field.id.toString} placeholder={field.placeholder} value={value}/>
                <span class="help-inline">
                    {req.errors.getOrElse(field.id.toString, "")}
                </span>
            </div>
        </div>
    }

    private def renderSelect(field: FormField, req: ScalapressRequest) = {

        val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
        val star = if (field.required) "*" else ""

        val opts = field.optionsList.map(opt =>
            <option>
                {opt}
            </option>)

        <div class={cssClass}>
            <label class="control-label">
                {field.name}{star}
            </label>
            <div class="controls">
                <select name={field.id.toString}>
                    {opts}
                </select>
                <span class="help-inline">
                    {req.errors.getOrElse(field.id.toString, "")}
                </span>
            </div>
        </div>
    }

    private def renderCheck(field: FormField) = {
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" name={field.id.toString}/>{field.name}
                </label>
            </div>
        </div>
    }

    private def renderChecks(field: FormField) = {
        val checks = field.optionsList.map(opt =>
            <label class="checkbox">
                <input type="checkbox" name={field.id.toString}/>{opt}
            </label>)


        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    {field.name}
                </label>{checks}
            </div>
        </div>
    }

    private def renderRadio(field: FormField, req: ScalapressRequest) = {

        val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
        val star = if (field.required) "*" else ""

        val radios = field.optionsList.map(opt =>
            <label class="radio">
                <input type="radio" name={field.id.toString} value={opt}/>{opt}
            </label>)

        <div class={cssClass}>
            <div class="controls">
                <label class="radio">
                    {field.name}{star}
                </label>{radios}<span class="help-inline">
                {req.errors.getOrElse(field.id.toString, "")}
            </span>
            </div>
        </div>
    }
}