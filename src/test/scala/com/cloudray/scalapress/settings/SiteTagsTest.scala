package com.cloudray.scalapress.settings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import tag._
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class SiteTagsTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    context.installationDao = mock[InstallationDao]
    val sreq = ScalapressRequest(req, context)

    val installation = new Installation
    Mockito.when(context.installationDao.get).thenReturn(installation)

    test("site name tag happy path") {
        installation.name = "my blog"
        assert("my blog" === new SiteNameTag().render(sreq, Map.empty).get)
    }

    test("site email tag happy path") {
        installation.email = "sam@sam.com"
        assert("<a href='mailto:sam@sam.com'>sam@sam.com</a>" === new SiteEmailTag().render(sreq, Map.empty).get)
    }

    test("site vat tag happy path") {
        installation.vatNumber = "12345"
        assert("12345" === new SiteVatTag().render(sreq, Map.empty).get)
    }

    test("site phone tag happy path") {
        installation.telephone = "07777889988"
        assert("07777889988" === new SitePhoneTag().render(sreq, Map.empty).get)
    }

    test("site postcode tag happy path") {
        installation.postcode = "sw109nh"
        assert("sw109nh" === new SitePostcodeTag().render(sreq, Map.empty).get)
    }

    test("site address tag happy path") {
        installation.address = "my house"
        assert("my house" === new SiteAddressTag().render(sreq, Map.empty).get)
    }

    test("site google map link happy path") {
        installation.postcode = "RG15 9GT"
        assert("<a href='http://maps.google.co.uk/maps?hl=en&safe=off&q=RG15 9GT'>RG15 9GT</a/>" === new
            SiteGoogleMapTag().render(sreq, Map.empty).get)
    }

    test("site address label tag happy path") {
        installation.address = "my house"
        installation.country = "england"
        installation.postcode = "RG15 9GT"
        assert("my house, RG15 9GT, england" === new SiteAddressLabelTag().render(sreq, Map.empty).get)
    }

    test("site address label seperator tag happy path") {
        installation.address = "my house"
        installation.country = "england"
        installation.postcode = "RG15 9GT"
        assert("my house&RG15 9GT&england" === new SiteAddressLabelTag().render(sreq, Map("sep" -> "&")).get)
    }

    test("site country tag happy path") {
        installation.country = "england"
        assert("england" === new SiteCountryTag().render(sreq, Map.empty).get)
    }

    test("site company number happy path") {
        installation.companyNumber = "CR445566"
        assert("CR445566" === new SiteCompanyNumberTag().render(sreq, Map.empty).get)
    }
}
