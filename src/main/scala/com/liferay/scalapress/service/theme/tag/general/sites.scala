package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
object SiteAddressTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        context.siteDao.findAll().headOption.map(_.address)
}

object SiteAddressLabelTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val sep = params.get("sep").getOrElse(", ")
        context.siteDao.findAll().headOption match {
            case None => None
            case Some(site) => {
                val a = new ArrayBuffer[String]()
                Option(site.address).foreach(add => a ++= add.split("\n"))
                Option(site.postcode).foreach(p => a += p)
                Option(site.country).foreach(c => a += c)
                Option(a.map(_.trim).mkString(sep))
            }
        }
    }
}

object SiteNameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.name)
}

object SiteCountryTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.country)
}

object SiteEmailTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        context.siteDao.findAll().headOption.map(site => "<a href='mailto:" + site.email + "'>" + site.email + "</a>")
}

object SitePhoneTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.telephone)
}

object SitePostcodeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.postcode)
}

object SiteVatTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.vatNumber)
}

object SiteCompanyNumberTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.companyNumber)
}

object SiteGoogleMapTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        context
          .siteDao
          .findAll()
          .headOption
          .map(site =>
            "<a href='http://maps.google.co.uk/maps?hl=en&safe=off&q=" + site.postcode + "'>" + site.postcode + "</a/>")
}