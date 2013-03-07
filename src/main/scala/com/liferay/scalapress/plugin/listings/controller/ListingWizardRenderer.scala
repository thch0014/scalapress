package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.service.{WizardRenderer, WizardStep}

/** @author Stephen Samuel */
object ListingWizardRenderer {

    //  val ChoosePackage = 1
    val SelectFolder = 1
    val ListingFields = 2
    val UploadImages = 3
    val Confirmation = 4
    val Completed = 5

    // WizardStep("/listing/package", "Package"),

    def steps = List(WizardStep("/listing/folder", "Folders"),
        WizardStep("/listing/field", "Details"),
        WizardStep("/listing/image", "Images"),
        WizardStep("/listing/confirmation", "Confirm"),
        WizardStep("#", "Completed"))

    def render(stage: Int) = WizardRenderer.render(steps, stage)
}

