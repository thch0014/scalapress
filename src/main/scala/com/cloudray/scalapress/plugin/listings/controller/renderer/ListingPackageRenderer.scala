package com.cloudray.scalapress.plugin.listings.controller.renderer

import xml.Unparsed
import com.cloudray.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage}

/** @author Stephen Samuel */
object ListingPackageRenderer {

    def render(packages: Seq[ListingPackage], plugin: ListingsPlugin) = {

        val text = Option(plugin.packagesPageText).getOrElse("")
        <div id="listing-process-packages">
            {Unparsed(text)}{_renderPackages(packages)}
        </div>
    }

    private def _renderPackages(packages: Seq[ListingPackage]) = {
        packages.map(pck => {
            val desc = Option(pck.description).getOrElse("")
            <div class="listing-package well">

                <div class="name lead">
                    <a href={"/listing/package/" + pck.id.toString}><i class="icon-edit"></i> 
                        {pck.name}
                    </a>
                </div>

                <div class="desc">
                    <p>
                        {Unparsed(desc)}
                    </p>
                </div>

                <div class="cost">
                    <span class="badge">
                        {pck.priceText}
                    </span>
                </div>
            </div>
        })
    }

}
