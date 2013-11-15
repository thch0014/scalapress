package com.cloudray.scalapress.util.mvc

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.springframework.http.MediaType

/** @author Stephen Samuel */
class ScalapressPageMessageConverterTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val renderer = mock[ScalapressPageRenderer]
  val converter = new ScalaPressPageMessageConverter(renderer)

  "a scalapress convertor" should "accept only text/html with ScalapressPage" in {
    for ( mediaType <- List(MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED,
      MediaType.APPLICATION_JSON, MediaType.APPLICATION_XHTML_XML, MediaType.TEXT_PLAIN);
          klass <- List(classOf[Object], classOf[String]) ) {
      assert(!converter.canRead(klass, mediaType))
      assert(!converter.canWrite(klass, mediaType))
    }
    assert(converter.canRead(classOf[ScalapressPage], MediaType.TEXT_HTML))
    assert(converter.canWrite(classOf[ScalapressPage], MediaType.TEXT_HTML))
  }
}
