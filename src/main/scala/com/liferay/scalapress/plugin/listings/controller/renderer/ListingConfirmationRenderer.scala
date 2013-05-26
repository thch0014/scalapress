package com.liferay.scalapress.plugin.listings.controller.renderer

import xml.Unparsed
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
class ListingConfirmationRenderer(context: ScalapressContext) {

    def render(process: ListingProcess) = {

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
            </table>{_complete(process)}
        </div>
    }

    def _complete(process: ListingProcess) = {
        <form method="POST" action="/listing/confirmation">
            <button type="submit" class="btn btn-primary">
                Complete
            </button>
        </form>
    }

    def _folders(process: ListingProcess) = {
        val names = process.folders.map(f => context.folderDao.find(f).name)
        names.mkString(", ")
    }

    def _images(process: ListingProcess) = {
        val links = process.imageKeys.map(key => context.imageService.imageLink(key, 160, 120))
        links.map(link => <img src={link}/>)
    }

    def _attributes(process: ListingProcess) = {
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
