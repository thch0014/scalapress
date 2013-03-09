package com.liferay.scalapress.service

import org.apache.commons.io.FilenameUtils
import com.liferay.scalapress.domain.Image

/** @author Stephen Samuel */
object ImageRenderer {

    def link(img: Image): String = "<img src='/images/" + img.filename + "' alt='" + img.alt + "' border='0'/>"

    def alt(filename: String) = {
        FilenameUtils.getBaseName(filename).replace('-', ' ').replace('_', ' ')
    }
}
