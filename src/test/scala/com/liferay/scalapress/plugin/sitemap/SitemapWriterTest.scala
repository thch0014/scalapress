package com.liferay.scalapress.plugin.sitemap

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
class SitemapWriterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val url1 = Url("http://coldplay/news", "2008-11-14", "weekly", 0.5)
    val url2 = Url("http://coldplay/live", "2012-12-14", "yearly", 0.9)

    val input = getClass.getResourceAsStream("/com/liferay/scalapress/plugin/sitemap/samplesitemap.xml")
    val sitemap = IOUtils.toString(input, "UTF-8")

    test("correct XML format") {
        val actual = SitemapWriter.write(Seq(url1, url2))
        assert(sitemap.replaceAll("\\s", "") === actual.replaceAll("\\s", ""))
    }
}
