package com.liferay.scalapress.plugin.staticcache

import javax.servlet._
import javax.servlet.http.{HttpServletResponse, HttpServletResponseWrapper, HttpServletRequest}
import java.io.{ByteArrayOutputStream, File}
import org.apache.commons.io.{FilenameUtils, FileUtils, IOUtils}
import com.liferay.scalapress.Logging
import org.apache.commons.io.output.TeeOutputStream
import org.springframework.web.context.support.WebApplicationContextUtils

/** @author Stephen Samuel */
class WriteoutCacheFilter extends Filter with Logging {

    var context: ServletContext = _
    var properties: WriteoutCacheProperties = _

    def destroy() {}

    def init(filterConfig: FilterConfig) {
        context = filterConfig.getServletContext

        properties = WebApplicationContextUtils
          .getWebApplicationContext(context)
          .getBean(classOf[WriteoutCacheProperties])

        if (properties.enabled) {
            val file = new File(context.getRealPath(properties.directory))
            logger.info("Using cache directory [{}]", properties.directory)
            if (!file.exists()) {
                logger.debug("Creating cache directory [{}]", properties.directory)
                file.mkdir()
            }
        }
    }

    def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        val uri = request.asInstanceOf[HttpServletRequest].getRequestURI

        // we only want to cache non-resource files, easist way is to look for files without an extension
        // as they will be proper spring controllers, and only ones without query param
        Option(FilenameUtils.getExtension(uri)) match {

            case Some(extension) if extension.length > 0 => chain.doFilter(request, response)
            case _ =>

                Option(request.asInstanceOf[HttpServletRequest].getQueryString) match {

                    case Some(query) => chain.doFilter(request, response)
                    case _ =>


                        val filename = uri.replace("-", "_").replace("/", "_")
                        val path = context.getRealPath(properties.directory + "/" + filename)
                        logger.debug("Cache check [{}]", path)
                        val file = new File(path)
                        if (file.exists && file.lastModified > System.currentTimeMillis - properties.timeout * 1000l) {

                            logger.debug("Cache hit [{}]", path)
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
                            FileUtils.write(file, output)
                        }
                }
        }
    }
}

class TeeServletOutputStream(tee: TeeOutputStream) extends ServletOutputStream {
    def write(b: Int) {
        tee.write(b)
    }
}