package com.liferay.scalapress.settings.tag

import com.liferay.scalapress.{Tag, ScalapressRequest}
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("comp_address")
class SiteAddressTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.address)
}

@Tag("comp_address_label")
class SiteAddressLabelTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        val sep = params.get("sep").getOrElse(", ")
        val a = new ArrayBuffer[String]()
        Option(request.installation.address).foreach(add => a ++= add.split("\n"))
        Option(request.installation.postcode).foreach(p => a += p)
        Option(request.installation.country).foreach(c => a += c)
        Option(a.map(_.trim).mkString(sep))
    }
}

@Tag("comp_name")
class SiteNameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.name)
}

@Tag("comp_country")
class SiteCountryTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.country)
}

@Tag("comp_email")
class SiteEmailTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) =
        Option(request.installation.email).map(email => "<a href='mailto:" + email + "'>" + email + "</a>")
}

@Tag("comp_telephone")
class SitePhoneTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.telephone)
}

@Tag("comp_postcode")
class SitePostcodeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.postcode)
}

@Tag("comp_vat")
class SiteVatTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.vatNumber)
}

@Tag("comp_number")
class SiteCompanyNumberTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = Option(request.installation.companyNumber)
}

@Tag("comp_postcode_gmap")
class SiteGoogleMapTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        Option("<a href='http://maps.google.co.uk/maps?hl=en&safe=off&q=" + request.installation.postcode + "'>" +
          request.installation.postcode + "</a/>")
    }
}