package com.cloudray.scalapress.plugin.staticcache

import javax.servlet._
import javax.servlet.http.{HttpServletResponse, HttpServletResponseWrapper, HttpServletRequest}
import java.io.{ByteArrayOutputStream, File}
import org.apache.commons.io.{FilenameUtils, FileUtils, IOUtils}
import com.cloudray.scalapress.Logging
import org.apache.commons.io.output.TeeOutputStream
import org.springframework.web.context.support.WebApplicationContextUtils

/** @author Stephen Samuel */
class WriteoutCacheFilter extends Filter with Logging {

    val MIN_CACHE_SIZE = 1000

    var context: ServletContext = _
    var properties: WriteoutCacheProperties = _

    def destroy() {}

    def init(filterConfig: FilterConfig) {
        context = filterConfig.getServletContext
        properties = WebApplicationContextUtils.getWebApplicationContext(context).getBean(classOf[WriteoutCacheProperties])
        if (properties.enabled) {
            _ensureCacheDirectoryCreated(_cacheDirectory)
        }
    }

    def _cacheHit(file: File) = file.exists && file.lastModified > System.currentTimeMillis - properties.timeout * 1000l
    def _cacheDirectory = new File(context.getRealPath(properties.directory))
    def _cacheFile(req: HttpServletRequest): File = {
        val uri = req.getRequestURI
        val filename = uri.replace("-", "_").replace("/", "_")
        val path = context.getRealPath(properties.directory) + "/" + filename
        new File(path)
    }

    def _ensureCacheDirectoryCreated(dir: File) {
        logger.info("Using cache directory [{}]", dir)
        if (!dir.exists()) {
            logger.debug("Creating cache directory [{}]", dir)
            dir.mkdir()
        }
    }

    def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        val req = request.asInstanceOf[HttpServletRequest]

        // we only want to cache non-resource files, easist way is to look for files without an extension
        // as they will be proper spring controllers, and only ones without query param
        if (properties.enabled && _isCacheable(request.asInstanceOf[HttpServletRequest])) {

            val file = _cacheFile(req)
            logger.debug("Cache file [{}]", file)
            if (_cacheHit(file)) {
                logger.debug("Cache hit [{}]", file)
                val input = FileUtils.openInputStream(file)
                IOUtils.copy(input, response.getOutputStream)

            } else {

                val branch = new ByteArrayOutputStream
                val tee = new TeeServletOutputStream(new TeeOutputStream(response.getOutputStream, branch))
                val wrapper = new HttpServletResponseWrapper(response.asInstanceOf[HttpServletResponse]) {
                    override def getOutputStream: ServletOutputStream = tee
                }
                chain.doFilter(request, wrapper)

                val output = IOUtils.toString(branch.toByteArray, "utf-8")
                logger.debug("Cache fail - Writing {} chars", output.length)
                if (output.length > MIN_CACHE_SIZE)
                    FileUtils.write(file, output)
            }

        }
        else {

            chain.doFilter(request, response)
        }
    }

    def _isCacheable(request: HttpServletRequest) = {
        val uri = request.getRequestURI
        val ext = FilenameUtils.getExtension(uri)
        if (uri.startsWith("/backoffice") || uri.startsWith("/login") || ext.length > 0 || request.getQueryString != null) false
        else true
    }
}

class TeeServletOutputStream(tee: TeeOutputStream) extends ServletOutputStream {
    def write(b: Int) {
        tee.write(b)
    }
}