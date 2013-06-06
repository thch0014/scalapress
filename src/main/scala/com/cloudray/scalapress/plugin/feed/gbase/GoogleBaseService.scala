package com.cloudray.scalapress.plugin.feed.gbase

import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.obj.{ObjectQuery, ObjectDao, Obj}
import com.cloudray.scalapress.settings.Installation
import com.cloudray.scalapress.media.AssetStore
import org.joda.time.{DateTimeZone, DateTime}

/** @author Stephen Samuel */
object GoogleBaseService extends Logging {

    def run(objectDao: ObjectDao,
            gfeedDao: GBaseFeedDao,
            installation: Installation,
            feed: GBaseFeed,
            assetStore: AssetStore) = {
        logger.debug("Running GBASE feed")

        val objs = _objects(objectDao)

        val file = new GoogleBaseBuilder(installation.domain, feed.productCategory, assetStore).csv(objs, feed)
        logger.debug("Gbase file generated [{}]", file)

        upload(file, feed)
        file.delete

        feed.lastRuntime = new DateTime(DateTimeZone.UTC).getMillis
        gfeedDao.save(feed)
    }

    def _objects(objectDao: ObjectDao): Seq[Obj] = {
        val q = new ObjectQuery
        q.pageSize = 100000
        q.status = Some(Obj.STATUS_LIVE)
        q.minPrice = Some(1)

        val objs = objectDao.search(q).results
        logger.debug("Retrieved {} objects", objs.size)
        objs
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
