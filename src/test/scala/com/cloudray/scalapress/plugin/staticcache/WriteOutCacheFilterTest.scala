package com.cloudray.scalapress.plugin.staticcache

import java.util.UUID
import org.apache.commons.io.FileUtils
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import java.io.File
import javax.servlet.ServletContext
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class WriteOutCacheFilterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val filter = new WriteoutCacheFilter
    filter.properties = new WriteoutCacheProperties
    filter.context = mock[ServletContext]

    val req = mock[HttpServletRequest]
    Mockito.when(req.getRequestURI).thenReturn("/some/path.html")

    filter.properties.directory = "super/dir"
    Mockito.when(filter.context.getRealPath("super/dir")).thenReturn("/home/sam/cache")

    test("cache directory uses servlet context to resolve real path") {
        val file = filter._cacheDirectory
        assert("/home/sam/cache" === file.getAbsolutePath)
    }

    test("cache file uses cache directory and request path") {
        val file = filter._cacheFile(req)
        assert("/home/sam/cache/_some_path.html" === file.getAbsolutePath)
    }

    test("cache hit fails on non existent file") {
        val file = new File("sammy")
        assert(!file.exists)
        assert(!filter._cacheHit(file))
    }

    test("cache hit fails on aged file") {
        val file = File.createTempFile("cache_test", "html")
        assert(file.exists)

        file.setLastModified(System.currentTimeMillis - 1000)
        filter.properties.timeout = 1

        assert(!filter._cacheHit(file))
    }

    test("cache hit succeedd on existing and fresh file") {

        val file = File.createTempFile("cache_test", "html")
        assert(file.exists)

        file.setLastModified(System.currentTimeMillis)
        filter.properties.timeout = 1

        assert(filter._cacheHit(file))
    }

    test("ensure cache directory creates the directory if it does not exist") {
        val dir = FileUtils.getTempDirectoryPath + "/" + UUID.randomUUID().toString
        val file = new File(dir)
        assert(!file.exists)
        filter._ensureCacheDirectoryCreated(file)
        assert(file.exists)
        file.deleteOnExit()
    }
}
