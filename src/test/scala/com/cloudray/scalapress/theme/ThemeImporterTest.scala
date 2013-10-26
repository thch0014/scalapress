package com.cloudray.scalapress.theme

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.AssetStore
import org.mockito.{Matchers, Mockito}
import java.io.InputStream

/** @author Stephen Samuel */
class ThemeImporterTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val assetStore = mock[AssetStore]
  val importer = new ThemeImporter(assetStore)

  "a theme importer" should "handle header" in {
    val in = getClass.getResourceAsStream("/com/cloudray/scalapress/theme/theme1.zip")
    val theme = importer.importTheme("sammy", in)
    assert(theme.header === "sammy")
  }

  "a theme importer" should "handle theme name" in {
    val in = getClass.getResourceAsStream("/com/cloudray/scalapress/theme/theme1.zip")
    val theme = importer.importTheme("somename", in)
    assert(theme.name === "somename")
  }

  "a theme importer" should "handle footer" in {
    val in = getClass.getResourceAsStream("/com/cloudray/scalapress/theme/theme1.zip")
    val theme = importer.importTheme("sammy", in)
    assert(theme.footer === "bobby")
  }

  "a theme importer" should "write assets to the asset store" in {
    val in = getClass.getResourceAsStream("/com/cloudray/scalapress/theme/theme1.zip")
    val theme = importer.importTheme("sammy", in)
    Mockito.verify(assetStore).add(Matchers.eq("cpsongs.jpg"), Matchers.any[InputStream])
  }
}
