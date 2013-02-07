package com.liferay.scalapress.plugin.account

/** @author Stephen Samuel */
object RegistrationRenderer {

    def renderChooseAccountType(plugin: AccountPlugin): String = ""

    def renderRegistrationPage(value: Any) = {
        <div class="container-fluid">
            <form class="form-horizontal">
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
