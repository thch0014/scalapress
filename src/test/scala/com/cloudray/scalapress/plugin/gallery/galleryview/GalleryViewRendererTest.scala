package com.cloudray.scalapress.plugin.gallery.galleryview

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, OneInstancePerTest}
import com.cloudray.scalapress.plugin.gallery.base.Gallery

/** @author Stephen Samuel */
class GalleryViewRendererTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a galleryview renderer" should "include params in the script when params are set" in {
    val gallery = new Gallery
    gallery.setParams("{width: 320}")
    val script = GalleryViewRenderer._generateScript(gallery)
    assert(script.contains("galleryView({width: 320})"))
  }

  "a galleryview renderer" should "not include params in the script when null" in {
    val gallery = new Gallery
    val script = GalleryViewRenderer._generateScript(gallery)
    assert(script.contains("galleryView()"))
  }

}
