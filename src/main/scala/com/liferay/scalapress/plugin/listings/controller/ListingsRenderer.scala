package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.service.FriendlyUrlGenerator
import com.liferay.scalapress.obj.Obj

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
                    <a href={"/listing/" + obj.id}>Edit</a>
                </td>
            </tr>)

        <table id="mylistings" class="table table-hover table-condensed">
            {elems}
        </table>
    }
}
