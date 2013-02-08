package com.liferay.scalapress.plugin.account

import org.springframework.validation.Errors

/** @author Stephen Samuel */
object RegistrationRenderer {

    def renderChooseAccountType(plugin: AccountPlugin) =
        <div class="container-fluid">Choose Account Type
        </div>

    def renderRegistrationPage(plugin: AccountPlugin, errors: Errors) = {
        <div class="registration">
            <form class="form-horizontal registration" method="POST">
                <div class="control-group">
                    <label for="email">
                        Your name
                    </label>
                    <input name="name" type="text" placeholder="Your real name"/>
                </div>
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