package com.liferay.scalapress.plugin.listings.controller.renderer

import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import org.joda.time.DateTime

/** @author Stephen Samuel */
object ListingsRenderer {

    def createListing = <a href="/listing/package" class="btn">
        <i class="icon-plus"></i>
        Add new listing</a>

    def myListings(objects: Iterable[Obj]) = {

        val elems = objects.map(obj => {

            val expiryString = if (obj.expiry == 0) "" else new DateTime(obj.expiry).toString("dd/MMM/yyyy")

            <tr>
                <td>
                    <a href={FriendlyUrlGenerator.friendlyUrl(obj)}>
                        {obj.name}
                    </a>
                </td>
                <td>
                    {obj.status}
                </td>
                <td>
                    {expiryString}
                </td>
                <td>
                    <a href={"/listing/" + obj.id} class="btn btn-mini">Edit</a>
                </td>
            </tr>
        })

        <table id="mylistings" class="table table-hover table-condensed">
            <tr>
                <th>Listing</th>
                <th>Status</th>
                <th>Expiry Date</th>
                <th>Edit</th>
            </tr>{elems}
        </table>
    }
}
