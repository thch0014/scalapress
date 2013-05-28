package com.cloudray.scalapress.media

import java.io.InputStream

/** @author Stephen Samuel */
trait AssetLifecycleListener {

    def onStore(key: String, input: InputStream): (String, InputStream)
    def onDelete(key: String): Boolean = true
}
