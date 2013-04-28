package com.liferay.scalapress.plugin.feed.gbase

import com.googlecode.genericdao.search.Search
import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.liferay.scalapress.Logging
import com.liferay.scalapress.obj.{ObjectDao, Obj}
import com.liferay.scalapress.settings.Installation
import com.liferay.scalapress.media.AssetStore
import org.joda.time.{DateTimeZone, DateTime}

/** @author Stephen Samuel */
object GoogleBaseService extends Logging {

    def run(objectDao: ObjectDao,
            gfeedDao: GBaseFeedDao,
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
        file.delete

        feed.lastRuntime = new DateTime(DateTimeZone.UTC).getMillis
        gfeedDao.save(feed)
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
