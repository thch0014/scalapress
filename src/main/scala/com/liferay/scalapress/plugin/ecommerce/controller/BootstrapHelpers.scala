package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.validation.Errors

/** @author Stephen Samuel */
object BootstrapHelpers {

    def controlGroup(id: String, label: String, value: String, errors: Errors) =
        <div class="control-group">
            <label class="control-label" for={id}>
                {label}
            </label>
            <div class="controls">
                <input type="text" name={id} class="input-xlarge" placeholder={label} value={value}/>
                <span class="help-inline">
                    {Option(errors.getFieldError(id)).map(_.getDefaultMessage).getOrElse("")}
                </span>
            </div>
        </div>
}
