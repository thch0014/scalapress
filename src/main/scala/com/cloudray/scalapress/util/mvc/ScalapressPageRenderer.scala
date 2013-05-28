package com.cloudray.scalapress.util.mvc

import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.tag.TagRenderer
import com.googlecode.htmlcompressor.compressor.HtmlCompressor

/** @author Stephen Samuel */
class ScalapressPageRenderer(context: ScalapressContext) {

    val compressor = new HtmlCompressor()

    def render(page: ScalapressPage): String = {

        val toolbar = page._toolbar.map(_.render).getOrElse("")
        val header = TagRenderer.render(page.theme.header, page.req)
        val footer = TagRenderer.render(page.theme.footer, page.req)

        val sb = new StringBuilder()

        sb.append(header.replace("<body>", s"<body>\n$toolbar\n"))
        page._body.filter(_ != null).map(_.toString).filter(_ != null).foreach(sb.append(_))
        sb.append(footer)

        val preCompressed = sb.toString()
        compressor.compress(preCompressed)
    }
}
