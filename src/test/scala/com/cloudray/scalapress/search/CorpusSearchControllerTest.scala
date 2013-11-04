package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.controller.CorpusSearchController
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class CorpusSearchControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new CorpusSearchController
    controller.context = new ScalapressContext
    controller.themeService = mock[ThemeService]
    controller.service = mock[CorpusSearchService]

    val req = mock[HttpServletRequest]

    test("search controller invokes corpus search") {
        Mockito.when(controller.service.search("coldplay")).thenReturn(Nil)
        controller.search(req, "coldplay")
        Mockito.verify(controller.service).search("coldplay")
    }

}
