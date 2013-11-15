package com.cloudray.scalapress.util.mvc

import com.cloudray.scalapress.theme.tag.TagRenderer
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class ScalapressPageRenderer(context: ScalapressContext) {

  val compressor = new HtmlCompressor()

  def render(page: ScalapressPage): String = {

    val toolbar = page._toolbar.map(_.render).getOrElse("")
    val header = TagRenderer.render(page.theme.header, page.sreq)
    val footer = TagRenderer.render(page.theme.footer, page.sreq)

    val sb = new StringBuilder()

    sb.append(header.replace("<body>", s"<body>\n$toolbar\n"))
    page._body.filter(_ != null).map(_.toString).filter(_ != null).foreach(sb.append)
    sb.append(footer)

    val preCompressed = sb.toString()
    compressor.compress(preCompressed)
  }
}
