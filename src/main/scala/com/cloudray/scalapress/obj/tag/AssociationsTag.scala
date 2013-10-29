package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.obj.Item

/** @author Stephen Samuel */
@Tag("associations")
class AssociationsTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.obj.flatMap(obj => {
      val associations = obj.associations.asScala.toSeq.filter(_.status == Item.STATUS_LIVE)
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
