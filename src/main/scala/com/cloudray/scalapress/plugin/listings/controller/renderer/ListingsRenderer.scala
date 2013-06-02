package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.obj.Obj
import org.joda.time.DateTime
import com.cloudray.scalapress.util.UrlGenerator

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
                    <a href={UrlGenerator.url(obj)}>
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
