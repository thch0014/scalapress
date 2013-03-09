package com.liferay.scalapress.plugin.listings.controller.process.renderer

import com.liferay.scalapress.plugin.listings.ListingProcess
import xml.Unparsed
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ListingConfirmationRenderer(context: ScalapressContext) {

    def completeForm(process: ListingProcess) =
        <div id="listing-process-nopaymentrequired">
            <form method="POST" action="/listing/payment/success">
                <button type="submit" class="btn btn-primary">
                    Complete
                </button>
            </form>
        </div>

    def render(process: ListingProcess) =
        <div id="listing-process-confirmation">
            <legend>
                Listing Confirmation
            </legend>
            <table class="table table-hover table-condensed">
                <tr>
                    <td>Package</td>
                    <td>
                        {process.listingPackage.name}&nbsp;{process.listingPackage.priceText}
                    </td>
                </tr>
                <tr>
                    <td>Title</td>
                    <td>
                        {process.title}
                    </td>
                </tr>
                <tr>
                    <td>Folders</td>
                    <td>
                        {_folders(process)}
                    </td>
                </tr>{_attributes(process)}<tr>
                <tr>
                    <td>Content</td>
                    <td>
                        {process.content}
                    </td>
                </tr>
                <td>Images</td>
                <td>
                    {_images(process)}
                </td>
            </tr>
            </table>
        </div>

    private def _folders(process: ListingProcess) = {
        val names = process.folders.map(f => context.folderDao.find(f).name)
        names.mkString(", ")
    }

    private def _images(process: ListingProcess) = {
        val links = process.imageKeys.map(key => context.imageService.imageLink(key, 160, 120))
        links.map(link => <img src={link}/>)
    }

    private def _attributes(process: ListingProcess) = {
        val values = process.attributeValues.asScala.toSeq
        val sorted = values.sortBy(_.attribute.position)
        sorted.map(av =>
            <tr>
                <td>
                    {Unparsed(av.attribute.name)}
                </td>
                <td>
                    {Unparsed(av.value)}
                </td>
            </tr>)

    }
}
