package com.liferay.scalapress.plugin.form.controller

import com.liferay.scalapress.plugin.form.Submission

/** @author Stephen Samuel */
object FormSubmissionTextRenderer {

    val DEFAULT = <p>Thank you for your submission. We will respond as soon as possible.</p>

    def render(custom: String, submission: Submission): String = {
        val text = Option(custom).getOrElse(DEFAULT)
        text.toString()
    }
}
