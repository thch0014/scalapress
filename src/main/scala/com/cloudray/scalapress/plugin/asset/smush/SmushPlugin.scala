package com.cloudray.scalapress.plugin.asset.smush

import java.io.{ByteArrayInputStream, InputStream}
import org.springframework.stereotype.Component
import org.apache.commons.io.IOUtils
import com.googlecode.htmlcompressor.compressor.{YuiCssCompressor, YuiJavaScriptCompressor}
import com.cloudray.scalapress.media.AssetLifecycleListener

/** @author Stephen Samuel */
@Component
class SmushPlugin extends AssetLifecycleListener {

  val js = new YuiJavaScriptCompressor()
  js.setPreserveAllSemiColons(true)
  js.setLineBreak(1000)
  js.setDisableOptimizations(true)

  val css = new YuiCssCompressor()
  css.setLineBreak(1000)

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
    val source = IOUtils.toString(input, "UTF-8")
    val minified = try {
      css.compress(source)
    } catch {
      case e: Exception => source
    }
    minified.getBytes("UTF-8")
  }

  def _minifyJs(input: InputStream) = {
    val source = IOUtils.toString(input, "UTF-8")
    val minified = try {
      css.compress(source)
    } catch {
      case e: Exception => source
    }
    minified.getBytes("UTF-8")
  }
}
