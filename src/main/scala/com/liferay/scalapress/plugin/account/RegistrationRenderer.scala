package com.liferay.scalapress.plugin.account

import org.springframework.validation.Errors

/** @author Stephen Samuel */
object RegistrationRenderer {

    def renderChooseAccountType(plugin: AccountPlugin): String = ""

    def renderRegistrationPage(plugin: AccountPlugin, errors: Errors) = {
        <div class="container-fluid">
            <form class="form-horizontal" method="POST">
                <div class="control-group">
                    <label for="email">
                        Email
                    </label>
                    <input name="email" type="email" placeholder="Email Address"/>
                </div>
                <div class="control-group">
                    <label for="password">
                        Password
                    </label>
                    <input name="password" type="password" placeholder="Password"/>
                </div>
                <button type="submit" class="btn">Register</button>
            </form>
        </div>
    }
}
