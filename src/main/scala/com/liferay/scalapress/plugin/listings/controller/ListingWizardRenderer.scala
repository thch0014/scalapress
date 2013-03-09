package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.service.{WizardRenderer, WizardStep}

/** @author Stephen Samuel */
object ListingWizardRenderer {

    val ChoosePackage = 1
    val SelectFolder = 2
    val ListingFields = 3
    val UploadImages = 4
    val Confirmation = 5
    val Completed = 6

    def steps = List(WizardStep("/listing/package", "Package"),
        WizardStep("/listing/folder", "Folders"),
        WizardStep("/listing/field", "Details"),
        WizardStep("/listing/image", "Images"),
        WizardStep("/listing/confirmation", "Confirm"),
        WizardStep("#", "Completed"))

    def render(stage: Int) = WizardRenderer.render(steps, stage)
}

