package com.liferay.scalapress.settings

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("comp_address")
class SiteAddressTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        context.siteDao.findAll().headOption.map(_.address)
}

@Tag("comp_address_label")
class SiteAddressLabelTag extends ScalapressTag with TagBuilder {
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

@Tag("comp_name")
class SiteNameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.name)
}

@Tag("comp_country")
class SiteCountryTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.country)
}

@Tag("comp_email")
class SiteEmailTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.email).map(email => "<a href='mailto:" + email + "'>" + email + "</a>")
}

@Tag("site_name")
class SitePhoneTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.telephone)
}

@Tag("comp_postcode")
class SitePostcodeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.postcode)
}

@Tag("comp_vat")
class SiteVatTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.vatNumber)
}

@Tag("comp_telephone")
class SiteCompanyNumberTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        Option(context.siteDao.get.companyNumber)
}

@Tag("comp_postcode_gmap")
class SiteGoogleMapTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) =
        context
          .siteDao
          .findAll()
          .headOption
          .map(site =>
            "<a href='http://maps.google.co.uk/maps?hl=en&safe=off&q=" + site.postcode + "'>" + site.postcode + "</a/>")
}