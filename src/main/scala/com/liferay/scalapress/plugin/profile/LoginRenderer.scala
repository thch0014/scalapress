package com.liferay.scalapress.plugin.profile

/** @author Stephen Samuel */
object LoginRenderer {

    def renderLogin =
        <form action='/j_spring_security_check' method='POST' class="form-horizontal">
            <div class="control-group">
                <label for="email">
                    Email
                </label>
                <input name="j_username" type="email" placeholder="Email Address"/>
            </div>
            <div class="control-group">
                <label for="password">
                    Password
                </label>
                <input name="j_password" type="password" placeholder="Password"/>
            </div>
            <button type="submit" name="submit" class="btn btn-success">Login</button>
            <a href="/register" class="btn btn-primary">Register</a>
            <a href="/password" class="btn btn-info">Forgotton Password</a>
        </form>
}

