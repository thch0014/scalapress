package com.cloudray.scalapress.theme

import java.io.InputStream
import java.util.zip.ZipInputStream
import com.cloudray.scalapress.media.AssetStore
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
@Autowired
class ThemeImporter(assetStore: AssetStore) {

  def importTheme(name: String, in: InputStream): Theme = {

    val theme = new Theme
    theme.name = name

    val zip = new ZipInputStream(in)
    var entry = zip.getNextEntry
    while (entry != null) {
      entry.getName match {
        case "header.html" => theme.header = IOUtils.toString(zip).trim
        case "footer.html" => theme.footer = IOUtils.toString(zip).trim
        case name: String => assetStore.add(name, zip)
      }
      entry = zip.getNextEntry
    }

    theme
  }
}
