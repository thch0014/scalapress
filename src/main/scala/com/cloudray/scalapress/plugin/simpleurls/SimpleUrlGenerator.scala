package com.cloudray.scalapress.plugin.simpleurls

import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel
  *
  *         Examples
  *
  *         www.countryshowguide.co.uk/f13-castles-in-wales
  *         www.countryshowguide.co.uk/o58-caerphilly-castle
  *
  **/
object SimpleUrlGenerator extends UrlGenerator {

    def url(folder: Folder): String = "/f" + folder.id + "-" + normalize(folder.name)
    def url(obj: Obj): String = "/o" + obj.id + "-" + normalize(obj.name)
}

