package com.liferay.scalapress.plugin.listings.controller.renderer

import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingProcess}

/** @author Stephen Samuel */
object ListingFoldersRenderer {

    def render(process: ListingProcess, plugin: ListingsPlugin, folders: Array[Folder]) = {
        val max = process.listingPackage.maxFolders match {
            case 0 => 1
            case x: Int => x
        }
        val sorted = folders.sortBy(_.name)
        val selects = (1 to max).map(arg => _select(sorted))
        <div id="listing-process-folders">
            {plugin.foldersPageText}
            <form method="POST">
                {selects}<button type="submit" class="btn">Continue</button>
            </form>
        </div>
    }

    private def _select(folders: Array[Folder]) =
        <div class="listing-folder-selection">
            <select name="folderId">
                <option value="">None</option>{_options(folders)}
            </select>
        </div>

    private def _options(folders: Array[Folder]) = {
        folders.map(f =>
            <option value={f.id.toString}>
                {f.name}
            </option>)
    }
}

