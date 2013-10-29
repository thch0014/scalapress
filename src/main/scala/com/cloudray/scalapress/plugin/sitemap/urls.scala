package com.cloudray.scalapress.plugin.sitemap

import collection.mutable.ListBuffer
import com.cloudray.scalapress.ScalapressContext
import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.item.ItemQuery
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
object UrlBuilder {

  def build(context: ScalapressContext): Seq[Url] = {

    val domain = "http://" + context.installationDao.get.domain.replace("http://", "")
    folders(context, domain) ++ objects(context, domain)
  }

  def objects(context: ScalapressContext, domain: String): List[Url] = {

    val query = new ItemQuery().withStatus("LIVE").withPageSize(50000)
    val objects = context.itemDao.search(query).results.filter(_.objectType.searchable)

    val urls = new ListBuffer[Url]
    for ( obj <- objects ) {
      val loc = domain + UrlGenerator.url(obj)
      val lastmod = new DateTime(obj.dateUpdated, DateTimeZone.UTC).toString("yyyy-MM-dd")
      urls.append(Url(loc, lastmod, "weekly", 0.8))
    }
    urls.toList
  }

  def folders(context: ScalapressContext, domain: String): List[Url] = {

    val folders = context.folderDao.findAll.filterNot(_.hidden)

    val urls = new ListBuffer[Url]
    for ( f <- folders ) {
      val loc = domain + UrlGenerator.url(f)
      val lastmod = new DateTime(f.dateUpdated, DateTimeZone.UTC).toString("yyyy-MM-dd")
      urls.append(Url(loc, lastmod, "weekly", 0.6))
    }
    urls.toList
  }
}

case class Url(loc: String, lastmod: String, changefreq: String, priority: Double)