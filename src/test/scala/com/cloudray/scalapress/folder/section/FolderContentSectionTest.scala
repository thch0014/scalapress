package com.cloudray.scalapress.folder.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.media.AssetStore
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class FolderContentSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.assetStore = mock[AssetStore]
  val req = ScalapressRequest(mock[HttpServletRequest], context)
  val section = new FolderContentSection

  test("section backoffice url is absolute") {
    assert(section.backoffice.startsWith("/backoffice/"))
  }

  test("section changes image urls to use asset baseUrl") {

    Mockito.when(context.assetStore.baseUrl).thenReturn("mycdn.com/media/") // http should be added

    section.content = <p>
      <img src="/images/coldplay.png"/>
      <img src="images/jethrotull.png"/>
    </p>.toString()

    val actual = section.render(req).get
    assert(actual.contains("<img src=\"http://mycdn.com/media//coldplay.png\"/>"))
    assert(actual.contains("<img src=\"http://mycdn.com/media//jethrotull.png\"/>"))
    assert(!actual.contains("images"))
  }
}
