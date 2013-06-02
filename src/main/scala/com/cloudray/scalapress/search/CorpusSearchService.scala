package com.cloudray.scalapress.search

/** @author Stephen Samuel
  *
  *         Implementations of this trait provide a way of searching for content in the system regardless of their type.
  *         As distinct from object search which is a strongly typed searched service, this kind of search is meant only for
  *         generic content and may stretch acrossmultiple tables / entities.
  *
  **/
trait CorpusSearchService {
    def search(query: String): Seq[CorpusResult]
}

case class CorpusResult(title: String, url: String, snippet: String)
