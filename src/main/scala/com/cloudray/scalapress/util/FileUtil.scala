package com.cloudray.scalapress.util

import java.io.{FileWriter, File, InputStream}
import org.apache.commons.io.{IOUtils, FilenameUtils}

/** @author Stephen Samuel */
object FileUtil {
  def writeToTempFile(in: InputStream, name: String): File = {
    val file = File.createTempFile(FilenameUtils.getBaseName(name), FilenameUtils.getExtension(name))
    file.deleteOnExit()
    IOUtils.copy(in, new FileWriter(file))
    file
  }
}
