package com.liferay.scalapress.feeds.gbase

import org.springframework.stereotype.Component
import com.liferay.scalapress.dao.ObjectDao
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.domain.Obj
import java.io.File
import org.apache.commons.io.{FileUtils, IOUtils}
import com.enterprisedt.net.ftp.FTPClient
import com.liferay.scalapress.Logging
import org.springframework.scheduling.annotation.Scheduled

/** @author Stephen Samuel */
@Component
class GoogleBaseTask extends Logging {

    @Autowired var objectDao: ObjectDao = _

    val Hostname = ""
    val Port = 21
    val Username = ""
    val Password = ""
    val OutputFileName = ""

    @Scheduled(cron = "0 0 8 * * *")
    def run() {
        logger.debug("Running GBASE feed")
        val objs = objectDao.search(new Search(classOf[Obj]).addFilterLike("status", "Live"))
        val file = GoogleBaseBuilder.csv(objs)
        logger.debug("Gbase file completed")
        upload(file)
        file.delete()
    }

    def upload(file: File) {

        val ftp = new FTPClient()
        ftp.setRemoteHost(Hostname)
        ftp.user(Username)
        ftp.password(Password)
        logger.debug("Connecting to the FTP server...")
        ftp.connect()
        logger.debug("...connected")

        val in = FileUtils.openInputStream(file)
        ftp.put(in, OutputFileName)
        logger.debug("File has been uploaded")
        IOUtils.closeQuietly(in)

        ftp.quit()
    }
}
