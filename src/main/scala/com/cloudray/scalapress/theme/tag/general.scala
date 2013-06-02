package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import org.joda.time.DateTime

/** @author Stephen Samuel */
@Tag("home")
class HomeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        val root = request.context.folderDao.root
        val text = params.get("text").getOrElse(root.name)
        Option(buildLink("/", text, params))
    }
}

@Tag("title")
class TitleTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {

        val excludeHome = params.contains("exhome")
        val home = request.folder.exists(_.parent == null)
        if (excludeHome && home)
            None
        else {
            val title = request.title.getOrElse("Welcome")
            Some(build(title, params))
        }
    }
}

@Tag("script")
class ScriptTag extends ScalapressTag with TagBuilder {

    val sources = Seq("//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js",
        "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.0/js/bootstrap.min.js",
        "/static/js/jquery.colorbox-min.js",
        "/static/js/jquery.easing.1.3.js",
        "/static/js/jquery.galleryview-3.0-dev.js",
        "/static/js/jquery.timers-1.2.js",
        "/static/js/jquery.eventCalendar.min.js",
        "/static/js/jquery.galleriffic.js",
        "/static/js/jquery.opacityrollover.js")

    def render(request: ScalapressRequest, params: Map[String, String]) = {
        val string = sources.map(src => <script src={src} type="text/javascript"></script>).mkString("\n")
        Some(string)
    }
}

@Tag("css")
class CssTag extends ScalapressTag with TagBuilder {

    val sources = Seq(
        "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css",
        "/static/css/jquery.galleryview-3.0-dev.css",
        "http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700",
        "/static/css/eventCalendar.css",
        "/static/css/eventCalendar_theme_responsive.css",
        "/static/css/galleriffic.css",
        "/static/css/colorbox.css")

    def render(request: ScalapressRequest, params: Map[String, String]) = {
        val string = sources.map(src => <link rel="stylesheet" type="text/css" href={src}/>).mkString("\n")
        Some(string)
    }
}

@Tag("copyright")
class CopyrightTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, params: Map[String, String]) = {
        Some("Copyright &copy; " + new DateTime().toString("yyyy"))
    }
}