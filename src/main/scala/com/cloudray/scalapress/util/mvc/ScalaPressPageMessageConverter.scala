package com.cloudray.scalapress.util.mvc

import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.http.{HttpInputMessage, HttpOutputMessage}
import org.apache.commons.io.IOUtils
import org.springframework.http.MediaType

/** @author Stephen Samuel */
class ScalaPressPageMessageConverter(renderer: ScalapressPageRenderer)
  extends AbstractHttpMessageConverter[ScalapressPage] {

  def writeInternal(page: ScalapressPage, outputMessage: HttpOutputMessage) {
    val output = renderer.render(page)
    IOUtils.write(output, outputMessage.getBody)
  }

  def supports(clazz: Class[_]) = classOf[ScalapressPage].isAssignableFrom(clazz)
  def readInternal(clazz: Class[_ <: ScalapressPage], inputMessage: HttpInputMessage) = throw new RuntimeException()

  override def canRead(mt: MediaType): Boolean = mt == MediaType.TEXT_HTML
  override def canRead(clazz: Class[_], mt: MediaType): Boolean = supports(clazz) && canRead(mt)
  override def canWrite(mt: MediaType): Boolean = mt == MediaType.TEXT_HTML
  override def canWrite(clazz: Class[_], mt: MediaType): Boolean = supports(clazz) && canWrite(mt)
  override def getDefaultContentType(t: ScalapressPage): MediaType = MediaType.TEXT_HTML
}
