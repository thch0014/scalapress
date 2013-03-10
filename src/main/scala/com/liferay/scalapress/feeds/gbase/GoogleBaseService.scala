package com.liferay.scalapress.feeds.gbase

import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.domain.Obj
import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.liferay.scalapress.Logging
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.feeds.FeedDao
import com.liferay.scalapress.domain.setup.Installation
import com.liferay.scalapress.service.asset.AssetStore

/** @author Stephen Samuel */
object GoogleBaseService extends Logging {

    def run(objectDao: ObjectDao,
            feedDao: FeedDao,
            installation: Installation,
            feed: GBaseFeed,
            assetStore: AssetStore) = {
        logger.debug("Running GBASE feed")

        val objs = objectDao
          .search(new Search(classOf[Obj])
          .addFilterLike("status", "Live")
          .addFilterGreaterThan("sellPrice", 0))
        logger.debug("Retrieved {} objects", objs.size)

        val file = new GoogleBaseBuilder(installation.domain, feed.productCategory, assetStore).csv(objs, feed)
        logger.debug("Gbase file generated [{}]", file)

        upload(file, feed)
  //      file.delete

        feed.lastRuntime = System.currentTimeMillis()
        feedDao.save(feed)
    }

    def upload(file: File, feed: GBaseFeed) {

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
    }
}
