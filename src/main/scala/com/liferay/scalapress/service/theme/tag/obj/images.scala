package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */

object ImagesTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val h = params.get("h").orElse(params.get("height")).getOrElse("120").toInt
        val w = params.get("w").orElse(params.get("width")).getOrElse("120").toInt
        val link = params.get("link")
        val limit = params.get("limit").getOrElse("1").toInt
        val images = request.obj.flatMap(_.images.asScala).take(limit)
        val rendered = images.map(i => {
            val tag = "<img src='/images/" + i.filename + "' height='" + h + "' width='" + w + "'/>"
            tag
        })
        Option(rendered.mkString("\n"))
    }
}

object ColorboxTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.obj.map(obj => {

            val images = obj.images.asScala.map(image => {
                val link = "images/" + image.filename
                <p>
                    <a class="colorboxgroup" href={link} title={obj.name}>
                        {obj.name}
                    </a>
                </p>
            }).map(_.toString()).mkString("\n")

            """<script> $(document).ready(function() {
                    $(".colorboxgroup").colorbox({ rel: 'colorboxgroup'} );
                </script>""" + images
        })
    }
}