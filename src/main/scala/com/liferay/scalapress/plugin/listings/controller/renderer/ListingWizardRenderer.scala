package com.liferay.scalapress.plugin.listings.controller.renderer

import collection.mutable.ListBuffer
import com.liferay.scalapress.util.{WizardStep, WizardRenderer}
import com.liferay.scalapress.plugin.listings.domain.ListingPackage

/** @author Stephen Samuel */
object ListingWizardRenderer {

    val STEP_SELECT_FOLDER = 2
    val STEP_ListingFields = 3
    val STEP_UploadImages = 4
    val STEP_Confirmation = 5
    val STEP_PAYMENT = 6

    def steps(lp: ListingPackage): Seq[WizardStep] = {

        val list = new ListBuffer[WizardStep]
        // list.append(WizardStep("/listing/package", "Package"))
        if (lp.maxFolders > 0)
            list.append(WizardStep("/listing/folder", "Sections"))
        list.append(WizardStep("/listing/field", "Details"))
        list.append(WizardStep("/listing/image", "Images"))
        list.append(WizardStep("/listing/confirmation", "Confirm"))
        list.append(WizardStep("/listing/payment", "Payment"))
        list.toList
    }

    def render(lp: ListingPackage, stage: Int) = WizardRenderer.render(steps(lp), stage)
}

