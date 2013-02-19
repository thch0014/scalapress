package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.service.{WizardRenderer, WizardStep}

/** @author Stephen Samuel */
object ListingWizardRenderer {

    val ChoosePackage = 1
    val SelectCategory = 2
    val ListingDetails = 3
    val Payment = 4
    val Completed = 5

    def steps = List(WizardStep("/listing/package", "Package"),
        WizardStep("/listing/category", "Categories"),
        WizardStep("/listing/detail", "Details"),
        WizardStep("/listing/payment", "Payment"),
        WizardStep("#", "Completed"))

    def render(stage: Int) = WizardRenderer.render(steps, stage)
}

