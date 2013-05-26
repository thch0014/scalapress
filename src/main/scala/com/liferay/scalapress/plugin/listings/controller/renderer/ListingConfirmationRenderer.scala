package com.liferay.scalapress.plugin.listings.controller.renderer

import scala.xml.{Node, Unparsed}
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
class ListingConfirmationRenderer(context: ScalapressContext) {

    def render(process: ListingProcess) = {

        val content = Option(process.content).filterNot(_.isEmpty).getOrElse("No content - <a href='/listing/field'>Edit now</a>")

        <div id="listing-process-confirmation">
            <legend>
                Listing Confirmation
            </legend>
            <table class="table table-hover table-condensed">
                <tr>
                    <td>
                        <b>Package</b>
                    </td>
                    <td>
                        {process.listingPackage.name}&nbsp;{process.listingPackage.priceText}
                    </td>
                </tr>
                <tr>
                    <td>
                        <b>Title</b>
                    </td>
                    <td>
                        {process.title}
                    </td>
                </tr>
                <tr>
                    <td>
                        <b>Folders</b>
                    </td>
                    <td>
                        {_folders(process)}
                    </td>
                </tr>{_attributes(process)}<tr>
                <tr>
                    <td>
                        <b>Content</b>
                    </td>
                    <td>
                        {Unparsed(content)}
                    </td>
                </tr> <td>
                    <b>Images</b>
                </td>
                <td>
                    {_images(process)}
                </td>
            </tr>
            </table>{_complete(process)}
        </div>
    }

    def _complete(process: ListingProcess) = {
        <form method="POST" action="/listing/confirmation">
            <button type="submit" class="btn">
                Complete
            </button>
        </form>
    }

    def _folders(process: ListingProcess) = {
        val names = process.folders.map(f => context.folderDao.find(f).name)
        names.mkString(", ")
    }

    def _images(process: ListingProcess): Seq[Node] = {
        process.imageKeys.size match {
            case 0 => Seq(Unparsed("None uploaded - <a href='/listing/image'>Upload image</a>"))
            case _ =>
                val links = process.imageKeys.map(key => context.imageService.imageLink(key, 160, 120))
                links.map(link => <img src={link}/>)
        }

    }

    def _attributes(process: ListingProcess) = {
        val values = process.attributeValues.asScala.toSeq
        val sorted = values.sortBy(_.attribute.position)
        sorted.map(av =>
            <tr>
                <td>
                    <b>
                        {Unparsed(av.attribute.name)}
                    </b>
                </td>
                <td>
                    {Unparsed(av.value)}
                </td>
            </tr>)

    }
}
