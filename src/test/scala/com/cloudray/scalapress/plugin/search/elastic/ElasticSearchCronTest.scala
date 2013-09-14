package com.cloudray.scalapress.plugin.search.elastic

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.search.elasticsearch.ElasticSearchCron
import org.springframework.scheduling.annotation.Scheduled

/** @author Stephen Samuel */
class ElasticSearchCronTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  "the search cron" should "have reasonable reindex time" in {
    val annotation = classOf[ElasticSearchCron].getMethod("run").getAnnotation(classOf[Scheduled])
    assert(annotation.fixedDelay() > 1000 * 60 * 25 || annotation.fixedRate() > 1000 * 60 * 25)
  }

}
