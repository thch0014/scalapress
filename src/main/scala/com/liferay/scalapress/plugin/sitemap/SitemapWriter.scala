package com.liferay.scalapress.plugin.sitemap

import org.jdom.{Namespace, Element, Document}

/** @author Stephen Samuel */
object SitemapWriter {

    val ns = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9")

    def write(urls: List[Url]): Document = {
        val doc = new Document(new Element("urlset", ns))
        for (url <- urls) {
            val element = new Element("url", ns)
            element.addContent(new Element("loc", ns).setText(url.loc))
            element.addContent(new Element("lastmod", ns).setText(url.lastmod))
            element.addContent(new Element("changefreq", ns).setText(url.changefreq))
            element.addContent(new Element("priority", ns).setText(url.priority.toString))
            doc.getRootElement.addContent(element)
        }
        doc
    }
}