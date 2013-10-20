package com.cloudray.scalapress.util

import scala.xml.{Node, Unparsed}

/** @author Stephen Samuel */
object WizardRenderer {

  def render(steps: Seq[WizardStep], active: WizardStep): Node = {

    var index = 0
    val renderedSteps = steps.map(step => {

      index = index + 1
      val css = if (active == step) "active" else ""
      <li class={css}>
        <a href={step.href}>
          <span class="label">
            {Unparsed(index.toString)}
          </span> &nbsp;{Unparsed(step.label)}
        </a>
      </li>

    })

    val rendered = renderedSteps.mkString

    <div>
      <ul class="nav nav-pills wizard">
        {Unparsed(rendered)}
      </ul>
    </div>
  }
}

case class WizardStep(href: String, label: String)