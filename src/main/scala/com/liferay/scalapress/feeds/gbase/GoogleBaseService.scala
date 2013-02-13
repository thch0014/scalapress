package com.liferay.scalapress.feeds.gbase

import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.domain.Obj
import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.liferay.scalapress.Logging
import com.liferay.scalapress.dao.ObjectDao

/** @author Stephen Samuel */
object GoogleBaseService extends Logging {

    def run(objectDao: ObjectDao, feed: GBaseFeed) {
        logger.debug("Running GBASE feed")

        val objs = objectDao
          .search(new Search(classOf[Obj])
          .addFilterLike("status", "Live")
          .addFilterGreaterThan("sellPrice", 0))
        logger.debug("Retrieved {} objects", objs.size)

        val file = GoogleBaseBuilder.csv(objs)
        logger.debug("Gbase file generated [{}]", file)

        upload(file, feed)
        file.delete()
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
