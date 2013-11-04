package com.cloudray.scalapress.plugin.feed.gbase

import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.cloudray.scalapress.item.{ItemQuery, ItemDao, Item}
import com.cloudray.scalapress.settings.Installation
import com.cloudray.scalapress.media.AssetStore
import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
object GoogleBaseService extends Logging {

  def run(objectDao: ItemDao,
          gfeedDao: GBaseFeedDao,
          installation: Installation,
          feed: GBaseFeed,
          assetStore: AssetStore) = {
    logger.debug("Running GBASE feed")

    val objs = _objects(objectDao)

    val file = new GoogleBaseBuilder(installation.domain, feed.productCategory, assetStore).csv(objs, feed)
    logger.debug("Gbase file generated [{}]", file)

    upload(file, feed) match {
      case true => logger.info("GBase upload completed")
      case false => logger.error("GBase upload failed")
    }
    file.delete

    feed.lastRuntime = new DateTime(DateTimeZone.UTC).getMillis
    gfeedDao.save(feed)
  }

  def _objects(objectDao: ItemDao): Seq[Item] = {
    val q = new ItemQuery
    q.pageSize = 100000
    q.status = Some(Item.STATUS_LIVE)
    q.minPrice = Some(1)

    val objs = objectDao.search(q).results
    logger.debug("Retrieved {} objects", objs.size)
    objs
  }

  def upload(file: File, feed: GBaseFeed): Boolean = {

    try {

      val ftp = new FTPClient()
      ftp.setRemoteHost(feed.ftpHostname)

      logger.debug("Connecting to the FTP server...")
      ftp.connect()
      logger.debug("...connected")

      ftp.user(feed.ftpUsername)
      ftp.password(feed.ftpPassword)

      logger.debug("Uploading file to {}...", feed.ftpFilename)
      val in = FileUtils.openInputStream(file)
      ftp.put(in, feed.ftpFilename)
      logger.debug("...file has been uploaded")

      IOUtils.closeQuietly(in)
      ftp.quit()

      true

    } catch {
      case e: Exception =>
        logger.warn("Could not complete upload {}", e.getMessage)
        false
    }
  }
}
