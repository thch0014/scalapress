package com.liferay.scalapress.plugin.sitemap

import collection.mutable.ListBuffer
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.controller.admin.obj.ObjectQuery
import com.liferay.scalapress.service.FriendlyUrlGenerator
import org.joda.time.DateTime

/** @author Stephen Samuel */
object UrlBuilder {

    def build(context: ScalapressContext): List[Url] = {

        val domain = "http://" + context.installationDao.get.domain.replace("http://", "")

        val query = new ObjectQuery(status = Some("Live"))
        val objects = context.objectDao.search(query)
        val urls = new ListBuffer[Url]
        for (obj <- objects.results) {
            val loc = domain + FriendlyUrlGenerator.friendlyUrl(obj)
            val lastmod = new DateTime(obj.dateUpdated).toString("yyyy-MM-dd")
            urls.append(Url(loc, lastmod, "weekly", 0.8))
        }
        urls.toList
    }
}

case class Url(loc: String, lastmod: String, changefreq: String, priority: Double)