package com.cloudray.scalapress.plugin.fbopengraph

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class OpenGraphImageTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext

  context.installationDao = mock[InstallationDao]
  context.assetStore = mock[AssetStore]

  val installation = new Installation
  installation.domain = "coldplay.com"
  installation.name = "coldplay tees"
  Mockito.when(context.installationDao.get).thenReturn(installation)

  Mockito.when(context.assetStore.link("coldplay.png")).thenReturn("http://coldplay.com/coldplay.png")

  val o = new Obj
  o.name = "big shirt"
  val sreq = new ScalapressRequest(req, context)

  val image = "coldplay.png"
  o.images.add(image)

  test("tag uses site name from installation") {
    val output = new OpenGraphImageTag().render(sreq.withObject(o), Map.empty)
    assert("<meta property=\"og:image\" content=\"http://coldplay.com/images/300/300/coldplay.png\"/>" === output.get)
  }
}
