package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess

/** @author Stephen Samuel */
object ListingImagesRenderer {
    def render(process: ListingProcess) =
        <div id="listing-process-images">
            <form method="POST" enctype="multipart/form-data">
                {_upload}<button type="submit">Continue</button>
            </form>
        </div>

    private def _upload =
        <div class="control-group">
            <label class="control-label">Upload Image</label>
            <div class="controls">
                <input type="file" name="upload"/>
            </div>
        </div>

}
