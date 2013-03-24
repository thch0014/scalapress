package com.liferay.scalapress.obj

import java.io.{FileWriter, File}
import com.csvreader.CsvWriter
import com.liferay.scalapress.{FriendlyUrlGenerator, Logging}
import org.apache.commons.lang.WordUtils
import scala.collection.JavaConverters._
import com.liferay.scalapress.media.AssetStore
import com.liferay.scalapress.plugin.feed.gbase.GBaseFeed

/** @author Stephen Samuel */
class ObjectExport(domain: String, assetStore: AssetStore) extends Logging {

    def csv(objs: Seq[Obj], feed: GBaseFeed) = {

        val filtered = filter(feed, objs)
        val file = File.createTempFile("objexport", "csv")
        file.deleteOnExit()
        logger.debug("Writing to temp file {}", file)

        val fileWriter = new FileWriter(file)
        val csv = new CsvWriter(fileWriter, ',')
        csv.writeRecord(headers)

        filtered.foreach(obj => {
            csv.writeRecord(row(obj))
        })

        csv.flush()
        csv.close()

        file
    }

    def filter(feed: GBaseFeed, obj: Seq[Obj]) = {
        obj.filter(_.status.toLowerCase != "Disabled")
    }

    def headers =
        Array("id",
            "name",
            "stock",
            "sellprice",
            "costprice",
            "content",
            "link",
            "image")

    def row(obj: Obj): Array[String] = {

        val name = WordUtils.capitalizeFully(obj.name)
        val formattedPrice = "%1.2f".format(obj.sellPriceInc / 100.0)

        Array(obj.id.toString,
            name,
            obj.stock.toString,
            formattedPrice,
            obj.costPrice.toString,
            content(obj.content),
            "http://" + domain + "/" + FriendlyUrlGenerator.friendlyUrl(obj),
            assetStore.link(obj.images.asScala.head.filename))
    }

    def content(string: String): String = string
      .replaceAll("<.*?>", "")
      .take(10000)
      .replace("\t", "")
      .replace("\n", "")
      .replace("\r", "")
}
