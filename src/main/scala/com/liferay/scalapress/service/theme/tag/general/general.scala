package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
object HomeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Some("/")
}

object TitleTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {

        val excludeHome = params.contains("exhome")
        val home = request.folder.exists(_.parent == null)
        if (excludeHome && home)
            None
        else {
            val folderName = request.folder.map(_.name)
            val objectName = request.obj.map(_.name)
            folderName.orElse(objectName).orElse(Some("Welcome")).map(name => build(name, params))
        }
    }
}

object ScriptTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val tag = """
          <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
          <script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/js/bootstrap.min.js"></script>
          <script src="//cdn.jsdelivr.net/colorbox/1.3.21/jquery.colorbox-min.js"></script>
          <script src="//cdn.jsdelivr.net/colorbox/1.3.21/jquery.colorbox.js"></script>
          <script src="/static/js/jquery.easing.1.3.js"></script>
          <script src="/static/js/jquery.galleryview-3.0-dev.js"></script>
          <script src="/static/js/jquery.timers-1.2.js"></script>"""
        Some(tag)
    }
}

object CssTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val tag = """<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet"/>
                     <link href="/static/css/jquery.galleryview-3.0-dev.css" rel="stylesheet"/>
                    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700' rel='stylesheet' type='text/css'/>"""
        Some(tag)
    }
}
