package com.liferay.scalapress.plugin.form

import scala.collection.JavaConverters._
import xml.Elem
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.enums.FormFieldType

/** @author Stephen Samuel */
object FormRenderer {

    private def action(form: Form, folderId: Any) = "/form/" + form.id + "?folder=" + folderId

    def render(form: Form, req: ScalapressRequest): String = {
        val fields = form.fields.asScala.sortBy(_.position)
        val renderedFields = fields.map(renderField(_, req))
        val folderId = req.folder.map(_.id.toString).getOrElse("0")

        <form method="POST" enctype="multipart/form-data" action={action(form, folderId)} class="form-horizontal">
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

        field.fieldType match {
            case FormFieldType.DropDownMenu => renderSelect(field, req)
            case FormFieldType.Text => renderText(field, req)
            case FormFieldType.TickBox => renderCheck(field, req)
            case FormFieldType.TickBoxes => renderChecks(field, req)
            case FormFieldType.Radio => renderRadio(field, req)
            case FormFieldType.Description =>
                <p>
                    {field.name}
                </p>
            case FormFieldType.Header =>
                <legend>
                    {field.name}
                </legend>
            case FormFieldType.Attachment => renderUpload(field)
            case FormFieldType.Email => renderText(field, req)
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

    private def renderCheck(field: FormField, req: ScalapressRequest) = {
        val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
        <div class={cssClass}>
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" name={field.id.toString}/>{field.name}<span class="help-inline">
                    {req.errors.getOrElse(field.id.toString, "")}
                </span>
                </label>
            </div>
        </div>
    }

    private def renderChecks(field: FormField, req: ScalapressRequest) = {
        val checks = field.optionsList.map(opt =>
            <label class="checkbox">
                <input type="checkbox" name={field.id.toString}/>{opt}
            </label>)

        val cssClass = "control-group" + (if (req.errors.contains(field.id.toString)) " error" else "")
        <div class={cssClass}>
            <div class="controls">
                <label class="checkbox">
                    {field.name}
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