package com.cloudray.scalapress.plugin.gallery

import javax.persistence.Embeddable

/** @author Stephen Samuel */
@Embeddable
case class GalleryImage(var key: String, var description: String)
