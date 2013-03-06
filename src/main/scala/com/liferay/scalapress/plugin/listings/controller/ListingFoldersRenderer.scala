package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess
import com.liferay.scalapress.domain.Folder

/** @author Stephen Samuel */
object ListingFoldersRenderer {

    def render(process: ListingProcess, folders: Array[Folder]) = {
        val selects = (1 to process.listingPackage.maxFolders).map(arg => _select(folders))
        <div id="listing-process-folders">
            <form method="POST" enctype="multipart/form-data">
                {selects}<button type="submit">Continue</button>
            </form>
        </div>
    }

    private def _select(folders: Array[Folder]) =
        <div class="listing-folder-selection">
            <select name="folderId">
                {_options(folders)}
            </select>
        </div>

    private def _options(folders: Array[Folder]) = {
        folders.map(f =>
            <option value={f.id.toString}>
                {f.fullName}
            </option>)
    }
}

