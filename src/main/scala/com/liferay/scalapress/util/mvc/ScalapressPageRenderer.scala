package com.liferay.scalapress.util.mvc

import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.theme.tag.TagRenderer

/** @author Stephen Samuel */
class ScalapressPageRenderer(context: ScalapressContext) {

    def render(page: ScalapressPage): String = {

        val sb = new StringBuilder()

        sb.append(page._toolbar.getOrElse(""))
        sb.append(TagRenderer.render(page.theme.header, page.req, context))
        page._body.filter(_ != null).map(_.toString).filter(_ != null).foreach(sb.append(_))
        sb.append(TagRenderer.render(page.theme.footer, page.req, context))
        sb.toString()
    }
}
