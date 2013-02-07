package com.liferay.scalapress.plugin.search

import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
class SearchIndexer extends Logging {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var service: SearchService = _

    @Transactional
    def index() {

        val objs = objectDao.search(new Search(classOf[Obj])
          .addFilterILike("status", "live")
          .setMaxResults(20000))

        logger.info("Indexing {} objects", objs.size)
        objs.foreach(service.index(_))
        logger.info("Indexing finished", objs.size)
    }

}
