package com.cloudray.scalapress.plugin.search.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.cloudray.scalapress.search.Search

/** @author Stephen Samuel */
object ElasticsearchCountBuilder extends ElasticsearchUtils {
  def build(search: Search) = {
    countall from INDEX query {
      ElasticsearchQueryBuilder.build(search)
    }
  }
}
