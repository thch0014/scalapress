package com.liferay.scalapress.plugin.listings.controller.renderer

import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingProcess}

/** @author Stephen Samuel */
object ListingImagesRenderer {
    def render(process: ListingProcess, plugin: ListingsPlugin) =
        <div id="listing-process-images">
            {plugin.imagesPageText}<form method="POST" enctype="multipart/form-data">
            {_upload}<button type="submit" class="btn btn-primary">Continue</button>
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
