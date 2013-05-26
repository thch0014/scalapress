package com.liferay.scalapress.util

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class WizardRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val step1 = WizardStep("linka", "first")
    val step2 = WizardStep("linkb", "second")
    val step3 = WizardStep("linkc", "third")
    val steps = Seq(step1, step2, step3)

    test("each step shows the link url") {
        val actual = WizardRenderer.render(steps, 3).toString()
        assert(actual.contains("linka"))
        assert(actual.contains("linkb"))
        assert(actual.contains("linkc"))
    }

    test("each step shows the link text") {
        val actual = WizardRenderer.render(steps, 3).toString()
        assert(actual.contains("first"))
        assert(actual.contains("second"))
        assert(actual.contains("third"))
    }

    test("active link is rendered with class active") {
        val actual = WizardRenderer.render(steps, 2).toString()
        assert( """(?s)<li class="">.*<li class="active">.*<li class="">""".r.findFirstIn(actual).isDefined)
    }

    test("active index out of range renders no li's with class active") {
        val actual = WizardRenderer.render(steps, 442).toString()
        assert(!actual.contains("active"))
    }
}
