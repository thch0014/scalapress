package com.cloudray.scalapress.plugin.search.elasticsearch

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/elasticsearch"))
class ElasticsearchController {

  @Autowired
  var indexer: ElasticSearchIndexer = _

  @ResponseBody
  @RequestMapping(value = Array("reindex"))
  def reindex = {
    indexer.index()
    "reindexing"
  }
}
