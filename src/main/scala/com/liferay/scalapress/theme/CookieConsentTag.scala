package com.liferay.scalapress.theme

import tag.ScalapressTag

abstract class CookieConsentTag extends ScalapressTag {

    def getRegex: String = "cookiecontent"

    //    def generate(context: RequestContext,
    //                 params: java.util.Map[String, String]) = """<script src='/static/js/jquery.cookie.js'></script>");
    //			sb.append("<script src='/static/js/jquery.cookiecuttr.js'></script>");
    //			sb.append("<script src='static/js/cookiecuttr.js'></script>");
    //			sb.append("<link rel='stylesheet' href='static/css/cookiecuttr.css'>");"""

}