package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingPackage

/** @author Stephen Samuel */
object ListingPackageRenderer {

    def render(packages: Seq[ListingPackage]) = {
        <div id="listing-packages">
            {_renderPackages(packages)}
        </div>
    }

    private def _renderPackages(packages: Seq[ListingPackage]) = {
        packages.map(pck => {
            <div class="package">
                <div class="name">
                    {pck.name} {pck.fee}
                </div>
            </div>
        })
    }

}
