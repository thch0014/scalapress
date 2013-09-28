package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils

/** @author Stephen Samuel */
object ImageRenderer {
  def link(src: String): String = "<img src='" + src + "' border='0'/>"
  def alt(filename: String): String = FilenameUtils.getBaseName(filename).replace('-', ' ').replace('_', ' ')
}
