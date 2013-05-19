package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.{Tag, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.theme.MarkupRenderer
import com.liferay.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("associations")
class AssociationsTag extends ScalapressTag {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

        request.obj.flatMap(obj => {
            val associations = obj.associations.asScala.toSeq
            associations.size match {
                case 0 => None
                case _ =>
                    params.get("markup")
                      .flatMap(id => Option(request.context.markupDao.find(id.toLong)))
                      .map(m => MarkupRenderer.renderObjects(associations, m, request))
            }
        })
    }
}
