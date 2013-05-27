package com.liferay.scalapress.plugin.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class OpenGraphSiteTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext

    context.installationDao = mock[InstallationDao]
    val installation = new Installation
    installation.name = "coldplay tees"
    Mockito.when(context.installationDao.get).thenReturn(installation)

    val sreq = new ScalapressRequest(req, context)

    test("tag uses title from installation") {
        val output = new OpenGraphSiteTag().render(sreq, Map.empty)
        assert("<meta property=\"og:site_name\" content=\"coldplay tees\"/>" === output.get)
    }

}
