package com.cloudray.scalapress.plugin.meta.dublincore

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class DublinCorePublisherTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = new ScalapressRequest(req, context)
  context.installationDao = mock[InstallationDao]
  val installation = new Installation
  installation.name = "karanka's live blog"
  Mockito.when(context.installationDao.get).thenReturn(installation)

  val tag = new DublinCorePublisherTag()

  "a DC.Title tag" should "use installation name as publisher" in {
    val output = tag.render(sreq, Map.empty)
    assert("<meta name=\"DC.Publisher\" content=\"karanka's live blog\"/>" === output.get)
  }

}
