package com.cloudray.scalapress.util

import org.springframework.validation.Errors

/** @author Stephen Samuel */
object BootstrapHelpers {

  def controlGroup(id: String, label: String, value: String, errors: Errors) = {
    val hasError = errors.hasFieldErrors(id)
    val error = if (hasError) errors.getFieldError(id).getDefaultMessage else ""
    val classes = if (hasError) "control-group error" else "control-group"
    <div class={classes}>
      <label class="col-lg-2 control-label" for={id}>
        {label}
      </label>
      <div class="col-lg-8">
        <input type="text" name={id} class="input-xlarge" placeholder={label} value={value}/>
        <span class="help-inline">
          {error}
        </span>
      </div>
    </div>
  }
}
