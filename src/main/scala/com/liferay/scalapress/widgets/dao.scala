package com.liferay.scalapress.widgets

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.folder.Folder
import scala.collection.JavaConverters._

/** @author Stephen Samuel */

trait WidgetDao extends GenericDao[Widget, java.lang.Long]

@Component
@Transactional
class WidgetDaoImpl extends GenericDaoImpl[Widget, java.lang.Long] with WidgetDao {
    override def findAll: List[Widget] = {
        val c = getSession.createCriteria(classOf[Widget])
        c.setCacheable(true)
        val results = c.list.asInstanceOf[java.util.List[Widget]]
        results.asScala.toList
    }
}