package com.liferay.scalapress.plugin.sitemap

import org.jdom.{Namespace, Element, Document}

/** @author Stephen Samuel */
object SitemapWriter {

    def write(urls: List[Url]): Document = {
        val doc = new
            Document(new Element("urlset", Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9")))
        for (url <- urls) {
            val element = new Element("Url")
            element.addContent(new Element("loc").setText(url.loc))
            element.addContent(new Element("lastmod").setText(url.lastmod))
            element.addContent(new Element("changefreq").setText(url.changefreq))
            element.addContent(new Element("priority").setText(url.priority.toString))
            doc.getRootElement.addContent(element)
        }
        doc
    }
}