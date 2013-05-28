package com.cloudray.scalapress.plugin.cookieconsent

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.{ScalapressRequest, Tag}

@Tag("cookiecontent")
class CookieConsentTag extends ScalapressTag {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        Some( """<script src='/static/js/jquery.cookie.js'></script>");
    			sb.append("<script src='/static/js/jquery.cookiecuttr.js'></script>");
    			sb.append("<script src='static/js/cookiecuttr.js'></script>");
    			sb.append("<link rel='stylesheet' href='static/css/cookiecuttr.css'>");""")
    }
}