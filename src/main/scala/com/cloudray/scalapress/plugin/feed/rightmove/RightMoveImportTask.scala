package com.cloudray.scalapress.plugin.feed.rightmove

import org.springframework.scheduling.annotation.Scheduled
import scala.util.Random
import com.cloudray.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.obj.{Item, ObjectQuery}

/** @author Stephen Samuel */
class RightMoveImportTask extends Runnable with Logging {

    val Outcodes = 1 to 3000

    @Autowired var context: ScalapressContext = _

    // every 1m run with a random outcode
    @Scheduled(fixedDelay = 1000l * 60)
    def run() {

        val outcode = Random.shuffle(Outcodes.toList).head
        val properties = new RightMoveScraper().scrape(outcode)
        logger.debug("Scraped {} properties", properties.size)

        for ( property <- properties ) {

            // see if property already exists
            val q = new ObjectQuery
            q.exernalReference = Some(property.id)
            val objs = context.objectDao.search(q)

            val obj = objs.results.headOption.getOrElse(new Item)

            obj.exernalReference = property.id
            obj.name = property.address
            obj.price = property.price

            logger.debug("Persisting imported right move object [{}]", obj)
        }
    }
}
