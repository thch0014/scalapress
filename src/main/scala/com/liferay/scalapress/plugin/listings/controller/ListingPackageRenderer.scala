package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingPackage

/** @author Stephen Samuel */
object ListingPackageRenderer {

    def render(packages: Seq[ListingPackage]) = {
        <div id="listing-process-packages">
            {_renderPackages(packages)}
        </div>
    }

    private def _renderPackages(packages: Seq[ListingPackage]) = {
        packages.map(pck => {
            <div class="listing-package">
                <div class="name">
                    <a href={"/listing/package/" + pck.id.toString}>
                        {pck.name}
                    </a>
                </div>
                <div class="desc">
                    {pck.description}
                </div>
                <div class="cost">
                    {pck.priceText}
                </div>
            </div>
        })
    }

}
