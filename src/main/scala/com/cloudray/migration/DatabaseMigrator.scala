package com.cloudray.migration

import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
@Component
@Autowired
class DatabaseMigrator(dataSource: DataSource) extends Logging {

  def execute(sql: String): Int = {
    val conn = dataSource.getConnection
    val stmt = conn.createStatement()
    try {
      stmt.executeUpdate(sql)
    } catch {
      case e: Exception =>
        logger.warn(e.getMessage)
        0
    } finally {
      stmt.close()
      conn.close()
    }
  }

  @PostConstruct
  def update() {

    val conn = dataSource.getConnection
    execute("UPDATE Gallery_images SET `key`= `images` WHERE `key` is null")
    execute("ALTER TABLE Gallery_images DROP images")
    conn.close()
  }
}
