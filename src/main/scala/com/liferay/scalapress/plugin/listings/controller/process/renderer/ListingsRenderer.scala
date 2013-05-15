package com.liferay.scalapress.plugin.listings.controller.process.renderer

import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */
object ListingsRenderer {

    def createListing = <a href="/listing/package" class="btn btn-info">Add new listing</a>

    def myListings(objects: Iterable[Obj]) = {

        val elems = objects.map(obj =>
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
                    {obj.status}
                </td>
                <td>
                    <a href={"/listing/" + obj.id}>Edit</a>
                </td>
            </tr>)

        <table id="mylistings" class="table table-hover table-condensed">
            {elems}
        </table>
    }
}
