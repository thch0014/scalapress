package com.liferay.scalapress.plugin.listings.controller.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.listings.domain.ListingPackage

/** @author Stephen Samuel */
class ListingWizardRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val lp = new ListingPackage

    test("images step is not included when max images is 0") {
        lp.maxImages = 0
        val steps = ListingWizardRenderer.steps(lp)
        assert(!steps.contains(ListingWizardRenderer.ImagesStep))
    }

    test("images step is included when max images >0") {
        lp.maxImages = 1
        val steps = ListingWizardRenderer.steps(lp)
        assert(steps.contains(ListingWizardRenderer.ImagesStep))
    }

    test("folders step is not included when max folders is 0") {
        lp.maxFolders = 0
        val steps = ListingWizardRenderer.steps(lp)
        assert(!steps.contains(ListingWizardRenderer.FoldersStep))
    }

    test("folders step is not included when max folders > 0 but folders == 1") {
        lp.maxFolders = 5
        lp.folders = "99"
        val steps = ListingWizardRenderer.steps(lp)
        assert(!steps.contains(ListingWizardRenderer.FoldersStep))

        lp.folders = "99,,,,"
        val steps2 = ListingWizardRenderer.steps(lp)
        assert(!steps2.contains(ListingWizardRenderer.FoldersStep))
    }

    test("folders step is not included when max folders > 0 but folders == 0") {
        lp.maxFolders = 5
        lp.folders = ""
        val steps = ListingWizardRenderer.steps(lp)
        assert(!steps.contains(ListingWizardRenderer.FoldersStep))
    }

    test("folders step is included when max folders > 0 and folders > 1") {
        lp.maxFolders = 1
        lp.folders = "5,6"
        val steps = ListingWizardRenderer.steps(lp)
        assert(steps.contains(ListingWizardRenderer.FoldersStep))

        lp.maxFolders = 4
        lp.folders = "5,6,8"
        val steps2 = ListingWizardRenderer.steps(lp)
        assert(steps2.contains(ListingWizardRenderer.FoldersStep))
    }

    test("payments step is not included when fee is 0") {
        lp.fee = 0
        val steps = ListingWizardRenderer.steps(lp)
        assert(!steps.contains(ListingWizardRenderer.PaymentStep))
    }

    test("payments step is included when fee >0") {
        lp.fee = 1
        val steps = ListingWizardRenderer.steps(lp)
        assert(steps.contains(ListingWizardRenderer.PaymentStep))

        lp.fee = 345
        val steps2 = ListingWizardRenderer.steps(lp)
        assert(steps2.contains(ListingWizardRenderer.PaymentStep))
    }
}
