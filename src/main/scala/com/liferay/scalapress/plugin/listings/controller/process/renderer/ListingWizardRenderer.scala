package com.liferay.scalapress.plugin.listings.controller.process.renderer

import com.liferay.scalapress.plugin.listings.ListingPackage
import collection.mutable.ListBuffer
import com.liferay.scalapress.util.{WizardStep, WizardRenderer}

/** @author Stephen Samuel */
object ListingWizardRenderer {

    val ChoosePackage = 1
    val SelectFolder = 2
    val ListingFields = 3
    val UploadImages = 4
    val Confirmation = 5
    val Completed = 6

    def steps(lp: ListingPackage): Iterable[WizardStep] = {

        val list = new ListBuffer[WizardStep]
        list.append(WizardStep("/listing/package", "Package"))
        if (lp.maxFolders > 0)
            list.append(WizardStep("/listing/folder", "Sections"))

        list.append(WizardStep("/listing/field", "Details"))
        list.append(WizardStep("/listing/image", "Images"))
        list.append(WizardStep("/listing/confirmation", "Confirm"))
        list.append(WizardStep("#", "Completed"))

        list.toList
    }

    def render(lp: ListingPackage, stage: Int) = WizardRenderer.render(steps(lp), stage)
}

