package com.liferay.scalapress.media.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.liferay.scalapress.util.mvc.UrlResolver
import com.liferay.scalapress.media.AssetStore
import java.io._
import com.googlecode.htmlcompressor.compressor.{YuiCssCompressor, YuiJavaScriptCompressor}
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/medialib"))
class MediaLibraryController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def list = "admin/media/library.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def upload(@RequestParam(value = "minify", defaultValue = "false") minify: Boolean,
               @RequestParam("upload") uploads: java.util.List[MultipartFile]): String = {
        for ( upload <- uploads.asScala.filter(_ != null).filter(!_.isEmpty) ) {

            if (upload.getOriginalFilename.toLowerCase.endsWith(".css")) {
                val minified = _minifyCss(upload.getInputStream)
                assetStore.put(upload.getOriginalFilename, new ByteArrayInputStream(minified))

            } else if (upload.getOriginalFilename.toLowerCase.endsWith(".js")) {
                val minified = _minifyJs(upload.getInputStream)
                assetStore.put(upload.getOriginalFilename, new ByteArrayInputStream(minified))

            } else {

                assetStore.put(upload.getOriginalFilename, upload.getInputStream)
            }
        }
        "redirect:" + UrlResolver.medialib
    }

    def _minifyCss(input: InputStream) = {
        val compressed = new YuiCssCompressor().compress(IOUtils.toString(input, "UTF-8"))
        compressed.getBytes("UTF-8")
    }

    def _minifyJs(input: InputStream) = {
        val compressed = new YuiJavaScriptCompressor().compress(IOUtils.toString(input, "UTF-8"))
        compressed.getBytes("UTF-8")
    }

    @ModelAttribute("assets") def assets(@RequestParam(value = "pageNumber",
        required = false,
        defaultValue = "1") pageNumber: Int,
                                         @RequestParam(value = "q", required = false) q: String) =
        assetStore.search(q, 50).toList.asJava
}
