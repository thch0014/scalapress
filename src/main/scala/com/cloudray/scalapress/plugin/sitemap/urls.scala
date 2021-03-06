package com.cloudray.scalapress.plugin.sitemap

import collection.mutable.ListBuffer
import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.item.ItemQuery
import com.cloudray.scalapress.framework.{Logging, UrlGenerator, ScalapressContext}

/** @author Stephen Samuel */
object UrlBuilder extends Logging {

  def build(context: ScalapressContext): Seq[Url] = {
    Option(context.installationDao.get.domain) match {
      case None =>
        logger.warn("No domain set on installation; cannot generate site map urls")
        Nil
      case Some(domain) =>
        val d = "http://" + domain.replace("http://", "")
        folders(context, d) ++ objects(context, d)
    }
  }

  def objects(context: ScalapressContext, domain: String): List[Url] = {

    val query = new ItemQuery().withStatus("LIVE").withPageSize(50000)
    val objects = context.itemDao.search(query).results.filter(_.itemType.searchable)

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