package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils

object MimeTools {

  def contentType(filename: String) = {
    val ext = FilenameUtils.getExtension(filename).toLowerCase
    MimeType.values.find(_.name.toLowerCase == ext) match {
      case None => "*/*"
      case Some(enum) => enum.contentType
    }
  }
}