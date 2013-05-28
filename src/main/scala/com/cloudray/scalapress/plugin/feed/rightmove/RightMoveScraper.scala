package com.cloudray.scalapress.plugin.feed.rightmove

import org.apache.http.impl.conn.{SchemeRegistryFactory, PoolingClientConnectionManager}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.net.URLEncoder
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
class RightMoveScraper {

    var MaxResults = 500

    val connManager = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault())
    connManager.setMaxTotal(200)
    connManager.setDefaultMaxPerRoute(200)
    val client = new DefaultHttpClient(connManager)

    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)

    def scrape(outcode: Int): Iterable[Property] = {

        val get = new
            HttpGet("https://m.rightmove.co.uk/rightmove/data/search.html?lid=OUTCODE" + URLEncoder
              .encode("^") + outcode + "&searchType=SALE&numberOfPropertiesRequested=" + MaxResults)
        val resp = client.execute(get)
        val string = IOUtils.toString(resp.getEntity.getContent, "ISO-8859-1")
        val wrapper = mapper.readValue(string, classOf[Wrapper])
        wrapper.searchResults.property
    }
}

case class Wrapper(searchResults: SearchResults)
