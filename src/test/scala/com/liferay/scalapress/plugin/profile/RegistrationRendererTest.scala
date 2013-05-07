package com.liferay.scalapress.plugin.profile

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.profile.controller.RegistrationForm
import org.springframework.validation.{FieldError, Errors}
import org.mockito.Mockito

/** @author Stephen Samuel */
class RegistrationRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val form = new RegistrationForm
    val plugin = new AccountPlugin
    val errors = mock[Errors]

    test("output happy path") {
        val actual = RegistrationRenderer.renderRegistrationPage(form, plugin, errors)
        assert(
            "<div class=\"registration\">\n            <form class=\"form-horizontal registration\" method=\"POST\">\n                <div class=\"control-group\">\n                    <label for=\"name\">\n                        Your name\n                    </label>\n                    <input name=\"name\" type=\"text\" placeholder=\"Your name\"/>\n                    <span class=\"help-inline\">\n                        \n                    </span>\n                </div>\n                <div class=\"control-group\">\n                    <label for=\"email\">\n                        Email\n                    </label>\n                    <input name=\"email\" type=\"email\" placeholder=\"Email Address\"/>\n                    <span class=\"help-inline\">\n                        \n                    </span>\n                </div>\n                <div class=\"control-group\">\n                    <label for=\"password\">\n                        Password\n                    </label>\n                    <input name=\"password\" type=\"password\" placeholder=\"Password\"/>\n                    <span class=\"help-inline\">\n                        \n                    </span>\n                </div>\n                <button type=\"submit\" class=\"btn\">Register</button>\n            </form>\n        </div>"
              .replaceAll("\\s", "") === actual.toString().replaceAll("\\s", ""))
    }

    test("output with name field errors") {
        Mockito.when(errors.getFieldError("name")).thenReturn(new FieldError("", "name", "some error"))
        val actual = RegistrationRenderer.renderRegistrationPage(form, plugin, errors)
        println(actual.toString().replaceAll("\\s", ""))
        assert(
            "<divclass=\"registration\"><formclass=\"form-horizontalregistration\"method=\"POST\"><divclass=\"control-group\"><labelfor=\"name\">Yourname</label><inputname=\"name\"type=\"text\"placeholder=\"Yourname\"/><spanclass=\"help-inline\">someerror</span></div><divclass=\"control-group\"><labelfor=\"email\">Email</label><inputname=\"email\"type=\"email\"placeholder=\"EmailAddress\"/><spanclass=\"help-inline\"></span></div><divclass=\"control-group\"><labelfor=\"password\">Password</label><inputname=\"password\"type=\"password\"placeholder=\"Password\"/><spanclass=\"help-inline\"></span></div><buttontype=\"submit\"class=\"btn\">Register</button></form></div>"
              .replaceAll("\\s", "") === actual.toString().replaceAll("\\s", ""))
    }
}
