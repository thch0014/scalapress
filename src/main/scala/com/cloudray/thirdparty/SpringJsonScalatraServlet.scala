package com.cloudray.thirdparty

import org.scalatra.{ApiFormats, RenderPipeline, ScalatraServlet}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module

/** @author Stephen Samuel */
abstract class SpringJsonScalatraServlet extends ScalatraServlet with ApiFormats {

  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new Hibernate4Module)

  override protected def renderPipeline: RenderPipeline = json orElse super.renderPipeline

  private def json: RenderPipeline = {
    case a: Any if responseFormat == "json" =>
      response.writer.print(mapper.writeValueAsString(a))
  }

  before() {
    contentType = formats("json")
  }
}
