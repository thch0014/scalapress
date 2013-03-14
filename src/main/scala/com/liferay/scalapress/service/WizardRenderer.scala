package com.liferay.scalapress.service

/** @author Stephen Samuel */
object WizardRenderer {

    def render(steps: Iterable[WizardStep], position: Int) = {

        var index = 0
        val links = steps.map(step => {
            index = index + 1
            val classes = if (index == position) "current" else ""
            val href = if (position >= index) step.href else "#"
            <a class={classes} href={href}>
                <span class="badge">
                    {index}
                </span>{step.label}
            </a>
        })

        <div class="wizard">
            {links}
        </div>
    }
}

case class WizardStep(href: String, label: String)