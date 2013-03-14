package com.liferay.scalapress.controller.admin.obj.tag

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.MarkupRenderer
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Tag("associations")
class AssociationsTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.obj.flatMap(obj => {
            val associations = obj.associations.asScala.toSeq
            associations.size match {
                case 0 => None
                case _ =>
                    params.get("markup")
                      .flatMap(id => Option(context.markupDao.find(id.toLong)))
                      .map(m => MarkupRenderer.renderObjects(associations, m, request, context))
            }
        })
    }
}
