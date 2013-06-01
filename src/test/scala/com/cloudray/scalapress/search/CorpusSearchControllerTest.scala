package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.controller.CorpusSearchController
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class CorpusSearchControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new CorpusSearchController
    controller.context = new ScalapressContext

}
