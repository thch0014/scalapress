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
    val entry = zip.getNextEntry
    while (entry != null) {
      entry.getName match {
        case "header.html" => theme.header = IOUtils.toString(zip)
        case "footer.html" => theme.footer = IOUtils.toString(zip)
        case name: String => assetStore.add(name, zip)
      }
    }

    theme
  }
}
