package com.liferay.scalapress.plugin.media.smush

import java.io.{ByteArrayInputStream, InputStream}
import org.springframework.stereotype.Component
import org.apache.commons.io.IOUtils
import com.googlecode.htmlcompressor.compressor.{YuiCssCompressor, YuiJavaScriptCompressor}
import com.liferay.scalapress.media.AssetLifecycleListener

/** @author Stephen Samuel */
@Component
class SmushPlugin extends AssetLifecycleListener {

    def onStore(key: String, input: InputStream): (String, InputStream) = {

        if (key.toLowerCase.endsWith(".css")) {
            val minified = _minifyCss(input)
            (key, new ByteArrayInputStream(minified))

        } else if (key.toLowerCase.endsWith(".js")) {
            val minified = _minifyJs(input)
            (key, new ByteArrayInputStream(minified))

        } else {
            (key, input)
        }
    }

    def _minifyCss(input: InputStream) = {
        val compressed = new YuiCssCompressor().compress(IOUtils.toString(input, "UTF-8"))
        compressed.getBytes("UTF-8")
    }

    def _minifyJs(input: InputStream) = {
        val compressed = new YuiJavaScriptCompressor().compress(IOUtils.toString(input, "UTF-8"))
        compressed.getBytes("UTF-8")
    }
}
