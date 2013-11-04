package com.cloudray.scalapress.search.tag

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("corpus_title")
class CorpusResultTitleTag extends ScalapressTag {
  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.title)
}

@Tag("corpus_url")
class CorpusResultUrlTag extends ScalapressTag {
  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.url)
}

@Tag("corpus_snippet")
class CorpusResultSnippetTag extends ScalapressTag {
  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = sreq.corpusResult.map(_.snippet)
}