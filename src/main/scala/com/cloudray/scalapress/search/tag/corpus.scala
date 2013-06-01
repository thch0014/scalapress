package com.cloudray.scalapress.search.tag

import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("corpus_pagename")
class CorpusResultPageNameTag extends ScalapressTag {
    def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.page.name)
}

@Tag("corpus_pageid")
class CorpusResultPageIdTag extends ScalapressTag {
    def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.page.id.toString)
}

@Tag("corpus_url")
class CorpusResultUrlTag extends ScalapressTag {
    def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.page.url)
}

@Tag("corpus_snippet")
class CorpusResultSnippetTag extends ScalapressTag {
    def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.snippet)
}