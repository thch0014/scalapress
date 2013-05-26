package com.liferay.scalapress.util

import scala.xml.{Node, Unparsed}

/** @author Stephen Samuel */
object WizardRenderer {

    def render(steps: Seq[WizardStep], position: Int): Node = {

        var index = 0
        val renderedSteps = steps.map(step => {

            index = index + 1

            val css = if (index == position) "active" else ""
            val href = if (position >= index) step.href else "#"

            <li class={css}>
                <a href={href}>
                    {step.label}
                </a>
            </li>

        })

        val rendered = renderedSteps.mkString(<span class="divider"> - </span>.toString())

        <ul class="breadcrumb">
            {Unparsed(rendered)}
        </ul>
    }
}

case class WizardStep(href: String, label: String)