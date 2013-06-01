package com.cloudray.scalapress.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.CorpusSearchService
import com.cloudray.scalapress.theme.{Markup, ThemeService, MarkupRenderer}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("corpus"))
class CorpusSearchController {

    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var service: CorpusSearchService = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def search(req: HttpServletRequest, @RequestParam(value = "q") q: String): ScalapressPage = {

        val results = service.search(q)

        val sreq = ScalapressRequest(req, context).withTitle("Search Results")
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        page.body("<!-- corpus search results: " + results.size + " results found -->")

        val markup = new Markup
        markup.body =
          """<div class="corpus_result">
                <div class="pagename">[corpus_pagename]</div>
                <div class="url"><a href="[corpus_url]">[corpus_url]</a></div>
                <div class="snippet">[corpus_snippet]</div>
             </div>"""

        if (markup == null) {
            page.body("<!-- search results: no corpus search markup found -->")
        } else {
            page.body(MarkupRenderer.renderCorpusResults(results, markup, sreq))
        }

        page
    }
}
