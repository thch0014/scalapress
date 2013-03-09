package com.liferay.scalapress.controller.web

import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.http.{HttpInputMessage, HttpOutputMessage}
import org.apache.commons.io.IOUtils
import org.springframework.http.MediaType

/** @author Stephen Samuel */
class ScalaPressPageMessageConverter(renderer: ScalapressPageRenderer)
  extends AbstractHttpMessageConverter[ScalaPressPage] {

    def writeInternal(page: ScalaPressPage, outputMessage: HttpOutputMessage) {
        val output = renderer.render(page)
        IOUtils.write(output, outputMessage.getBody)
    }

    def supports(clazz: Class[_]) = classOf[ScalaPressPage].isAssignableFrom(clazz)
    def readInternal(clazz: Class[_ <: ScalaPressPage], inputMessage: HttpInputMessage) = throw new RuntimeException()

    override def canRead(mediaType: MediaType): Boolean = mediaType == MediaType.TEXT_HTML
    override def canRead(clazz: Class[_], mediaType: MediaType): Boolean = supports(clazz) && mediaType == MediaType
      .TEXT_HTML
    override def canWrite(mediaType: MediaType): Boolean = mediaType == MediaType.TEXT_HTML
    override def canWrite(clazz: Class[_], mediaType: MediaType): Boolean = supports(clazz) && mediaType == MediaType
      .TEXT_HTML
    override def getDefaultContentType(t: ScalaPressPage): MediaType = MediaType.TEXT_HTML
}
