package com.liferay.scalapress.util

import scala.xml.{Node, Unparsed}

/** @author Stephen Samuel */
object WizardRenderer {

    def render(steps: Seq[WizardStep], position: Int): Node = {

        var index = 0
        val renderedSteps = steps.map(step => {

            index = index + 1
            val css = if (index == position) "active" else ""
            <li class={css}>
                {step.label}<span class="chevron"></span>
            </li>

        })

        val rendered = renderedSteps.mkString

        <div class="wizard">
            <ul class="steps">
                {Unparsed(rendered)}
            </ul>
        </div>

    }
}

case class WizardStep(href: String, label: String)