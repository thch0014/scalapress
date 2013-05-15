package com.liferay.scalapress.plugin.navigation.rdfabreadcrumb

import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import scala.collection.mutable.{ListBuffer, ArrayBuffer}
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
@Tag("rdfa_breadcrumb")
class RdfaBreacrumbTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val sep = params.get("sep").getOrElse(" / ")
        val excludeHome = params.contains("exhome")
        val home = request.folder.exists(_.parent == null)
        if (excludeHome && home) {
            None
        } else {
            request.folder.map(folder => {

                val parents = new ListBuffer[Folder]
                var parent = folder.parent
                while (parent != null) {
                    parents.prepend(parent)
                    parent = parent.parent
                }

                val cssClass = params.get("class").getOrElse("breadcrumb")
                val tag = params.get("tag").getOrElse("ul")

                val buffer = new ArrayBuffer[String]
                buffer.append("<" + tag + " class='" + cssClass + "'>")
                buffer.append("<div xmlns:v='http://rdf.data-vocabulary.org/#'>")

                for ( parent <- parents ) {
                    buffer.append("<span typeof='v:Breadcrumb'>")
                    buffer.append("<span class='parent'>" + FriendlyUrlGenerator.friendlyLink(parent) + "</span>")
                    buffer.append("<span class='divider'>" + sep + "</span>")
                    buffer.append("<span rel='v:child'>")
                }

                buffer.append("<span class='active'>" + folder.name + "</span>")
                buffer.append("</span>")
                for ( parent <- parents ) {
                    buffer.append("</span>")
                }

                buffer.append("</div>")
                buffer.append("</" + tag + ">")
                buffer.mkString("\n")
            })
        }
    }
}