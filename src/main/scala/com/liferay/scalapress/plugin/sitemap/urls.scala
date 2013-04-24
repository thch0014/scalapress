package com.liferay.scalapress.plugin.sitemap

import collection.mutable.ListBuffer
import com.liferay.scalapress.{FriendlyUrlGenerator, ScalapressContext}
import org.joda.time.{DateTimeZone, DateTime}
import com.liferay.scalapress.obj.ObjectQuery

/** @author Stephen Samuel */
object UrlBuilder {

    def build(context: ScalapressContext): List[Url] = {

        val domain = "http://" + context.installationDao.get.domain.replace("http://", "")

        val query = new ObjectQuery().withStatus("LIVE").withPageSize(50000)
        val objects = context.objectDao.search(query).results.filter(_.objectType.searchable)
        val urls = new ListBuffer[Url]
        for ( obj <- objects ) {
            val loc = domain + FriendlyUrlGenerator.friendlyUrl(obj)
            val lastmod = new DateTime(obj.dateUpdated, DateTimeZone.UTC).toString("yyyy-MM-dd")
            urls.append(Url(loc, lastmod, "weekly", 0.8))
        }
        urls.toList
    }
}

case class Url(loc: String, lastmod: String, changefreq: String, priority: Double)