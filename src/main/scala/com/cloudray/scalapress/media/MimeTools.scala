package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils
import com.cloudray.scalapress.enums.MimeType
import javax.ws.rs.core.MediaType

object MimeTools {

    def contentType(filename: String) = {
        val ext = FilenameUtils.getExtension(filename).toLowerCase
        MimeType.values.find(_.name.toLowerCase == ext) match {
            case None => MediaType.WILDCARD
            case Some(enum) => enum.contentType
        }
    }
}