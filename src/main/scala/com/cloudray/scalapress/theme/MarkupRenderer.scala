package com.cloudray.scalapress.theme

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import tag.TagRenderer
import com.cloudray.scalapress.search.CorpusResult
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{BasketLine, OrderLine}

/** @author Stephen Samuel */
object MarkupRenderer extends Logging {

  def render(lines: Seq[BasketLine], m: Markup, r: ScalapressRequest): String = {
    require(m != null)
    val start = TagRenderer.render(m.start, r)

    // we must have null object otherwise the outer object will override tags used on basket lines that check
    // first for an object
    val body = lines
      .map(l => TagRenderer.render(m.body, r.withLine(l).withItem(null)))
      .mkString(TagRenderer.render(m.between, r))

    val end = TagRenderer.render(m.end, r)
    start + body + end
  }

  def render(m: Markup, request: ScalapressRequest): String = {
    val start = TagRenderer.render(m.start, request)
    val body = TagRenderer.render(m.body, request)
    val end = TagRenderer.render(m.end, request)
    start + body + end
  }

  def renderCorpusResults(results: Seq[CorpusResult], m: Markup, sreq: ScalapressRequest): String = {
    val start = TagRenderer.render(m.start, sreq)
    val body = results
      .map(r => TagRenderer.render(m.body, sreq.withResult(r)))
      .mkString(TagRenderer.render(m.between, sreq))
    val end = TagRenderer.render(m.end, sreq)
    start + body + end
  }

  def render(f: Folder, m: Markup, sreq: ScalapressRequest): String = render(m, sreq.withFolder(f))
  def render(o: Item, m: Markup, sreq: ScalapressRequest): String = render(m, sreq.withItem(o))

  def renderOrderLines(objects: Seq[OrderLine], m: Markup, request: ScalapressRequest) = {
    val start = TagRenderer.render(m.start, request)
    val body = objects
      .map(o => TagRenderer.render(m.body, request.withOrderLine(o)))
      .mkString(TagRenderer.render(m.between, request))
    val end = TagRenderer.render(m.end, request)
    start + body + end
  }

  def renderObjects(objects: Seq[Item], m: Markup, request: ScalapressRequest) = {
    val start = TagRenderer.render(m.start, request)
    val body = objects
      .map(o => TagRenderer.render(m.body, request.withItem(o)))
      .mkString(TagRenderer.render(m.between, request))
    val end = TagRenderer.render(m.end, request)
    start + body + end
  }

  def renderFolders(folders: Seq[Folder], m: Markup, request: ScalapressRequest): String = {
    val start = TagRenderer.render(m.start, request)
    val body = folders
      .map(f => TagRenderer.render(m.body, request.withFolder(f)))
      .mkString(TagRenderer.render(m.between, request))
    val end = TagRenderer.render(m.end, request)
    start + body + end
  }
}
