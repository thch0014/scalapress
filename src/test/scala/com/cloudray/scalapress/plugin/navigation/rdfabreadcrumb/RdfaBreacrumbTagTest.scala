package com.cloudray.scalapress.plugin.navigation.rdfabreadcrumb

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class RdfaBreacrumbTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val f1 = new Folder
    f1.id = 5
    f1.name = "food"

    val f2 = new Folder
    f2.id = 14
    f2.name = "spaghetti"
    f2.parent = f1

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    val sreq = ScalapressRequest(req, context).withFolder(f2)

    test("rdfa markup") {
        val rendered = new RdfaBreacrumbTag().render(sreq, Map.empty)
        assert(
            "<ul class='breadcrumb'><div xmlns:v='http://rdf.data-vocabulary.org/#'><span typeof='v:Breadcrumb'><span class='parent'><a href='/folder-5-food'rel=\"v:url\" property=\"v:title\">food</a></span><span class='divider'> / </span><span rel='v:child'><span typeof='v:Breadcrumb'><span class='active'>spaghetti</span></span></span></span></div></ul>"
              === rendered.get
              .replaceAll("\\n", "")
              .replaceAll("\\r", "")
              .replaceAll("\\s{2,}", ""))
    }
}
