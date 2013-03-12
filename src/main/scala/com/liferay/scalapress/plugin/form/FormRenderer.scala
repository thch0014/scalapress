package com.liferay.scalapress.plugin.form

import scala.collection.JavaConverters._
import xml.{Unparsed, Elem}
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.enums.{FieldSize, FormFieldType}

/** @author Stephen Samuel */
object FormRenderer {

    private def action(form: Form, folderId: Any) = "/form/" + form.id + "?folder=" + folderId

    def render(form: Form, req: ScalapressRequest): String = {
        val fields = form.fields.asScala.sortBy(_.position)
        val renderedFields = fields.map(renderField(_, req))
        val folderId = req.folder.map(_.id.toString).getOrElse("0")
        val captchaError = req.hasError("captcha.error")

        <form method="POST" enctype="multipart/form-data" action={action(form, folderId)} class="form-horizontal">
            {if (captchaError) <div class='alert alert-error'>Please try the captcha again</div> else null}{renderedFields}{if (form
          .captcha) _captcha()
        else ""}{button(form)}
        </form>.toString()
    }

    def button(form: Form) = {
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">
                    {Option(form.submitButtonText).filter(_.trim.length > 0).getOrElse("Submit")}
                </button>
            </div>
        </div>
    }

    private def _captcha() = {
        <div class="control-group">
            <div class="controls" style="height: 120px">

                <script type="text/javascript"
                        src="http://www.google.com/recaptcha/api/challenge?k=6LeFAt0SAAAAAE5ErgvudIK-jdUZGXbVOe7WP0Oq">
                </script>
                <noscript>
                    <iframe src="http://www.google.com/recaptcha/api/noscript?k=6LeFAt0SAAAAAE5ErgvudIK-jdUZGXbVOe7WP0Oq"
                            height="300" width="500" frameborder="0"></iframe> <br/>
                    <textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
                    <input type="hidden" name="recaptcha_response_field" value="manual_challenge"/>
                </noscript>
            </div>
        </div>
    }

    private def renderField(field: FormField, req: ScalapressRequest): Elem = {
        field.fieldType match {
            case FormFieldType.DropDownMenu => renderSelect(field, req)
            case FormFieldType.TickBox => renderCheck(field)
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
            case FormFieldType.Attachment => renderUpload(field)
            case _ => renderText(field, req)
        }
    }

    private def renderUpload(field: FormField) = {
        val star = if (field.required) "*" else ""
        <div class="control-group">
            <label class="control-label">
                {Unparsed(field.name)}{star}
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
        val css = Option(field.size).getOrElse(Some(FieldSize.Medium)) match {
            case FieldSize.Small => "input-small"
            case FieldSize.Large => "input-large"
            case FieldSize.XLarge => "input-xlarge"
            case FieldSize.XXLarge => "input-xxlarge"
            case _ => "input-medium"
        }

        <div class={cssClass}>
            <label class="control-label">
                {Unparsed(field.name)}{star}
            </label>
            <div class="controls">
                <input type="text" name={field.id.toString} placeholder={field.placeholder} value={value} class={css}/>
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
        </div>
    }

    private def renderCheck(field: FormField) = {
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" name={field.id.toString}/>{Unparsed(field.name)}
                </label>
            </div>
        </div>
    }

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