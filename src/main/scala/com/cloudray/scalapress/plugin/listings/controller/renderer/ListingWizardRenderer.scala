package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.util.{WizardStep, WizardRenderer}
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import scala.collection.mutable.ListBuffer

/** @author Stephen Samuel */
object ListingWizardRenderer {

    object FoldersStep extends WizardStep("/listing/folder", "Sections")
    object DetailsStep extends WizardStep("/listing/field", "Details")
    object ImagesStep extends WizardStep("/listing/image", "Images")
    object ConfirmationStep extends WizardStep("/listing/confirmation", "Confirm")
    object PaymentStep extends WizardStep("/listing/payment", "Payment")

    def steps(lp: ListingPackage) = {
        val buffer = new ListBuffer[WizardStep]
        if (lp.maxFolders > 0 && lp.folders.split(",").size > 1)
            buffer.append(FoldersStep)
        buffer.append(DetailsStep)
        if (lp.maxImages > 0)
            buffer.append(ImagesStep)
        buffer.append(ConfirmationStep)
        if (lp.fee > 0)
            buffer.append(PaymentStep)
        buffer.toList
    }

    def render(lp: ListingPackage, active: WizardStep) = WizardRenderer.render(steps(lp), active)
}



