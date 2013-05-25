package com.liferay.scalapress.plugin.listings.controller.renderer

import xml.Unparsed
import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage}

/** @author Stephen Samuel */
object ListingPackageRenderer {

    def render(packages: Seq[ListingPackage], plugin: ListingsPlugin) = {
        <div id="listing-process-packages">
            {Unparsed(plugin.packagesPageText)}{_renderPackages(packages)}
        </div>
    }

    private def _renderPackages(packages: Seq[ListingPackage]) = {
        packages.map(pck => {
            <div class="listing-package well">

                <div class="name lead">
                    <a href={"/listing/package/" + pck.id.toString}>
                        {pck.name}
                    </a>
                </div>

                <div class="desc">
                    <p>
                        {Unparsed(pck.description)}
                    </p>
                </div>

                <div class="cost">
                    <span class="badge badge-info">
                        {pck.priceText}
                    </span>
                </div>
            </div>
        })
    }

}
