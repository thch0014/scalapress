package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.domain.{Gallery, Image}

/** @author Stephen Samuel */
trait ImageDao {
    def findForObject(objId: Long): Array[Image]
    def get(id: Long): Image
}

@Component
@Transactional
class ImageDaoImpl extends HibernateBaseDAO with ImageDao {

    @Autowired
    override def setSessionFactory(sessionFactory: SessionFactory) {
        super.setSessionFactory(sessionFactory)
    }

    @Transactional
    override def get(id: Long) = getSession.get(classOf[Image], id).asInstanceOf[Image]

    @Transactional
    override def findForObject(objId: Long): Array[Image] = {
        getSession
          .createSQLQuery("select * from images where item=?")
          .addEntity("images", classOf[Image])
          .setLong(0, objId)
          .setMaxResults(20)
          .list()
          .asScala
          .map(_.asInstanceOf[Image])
          .toArray
    }
}

trait GalleryDao extends GenericDao[Gallery, java.lang.Long]

@Component
@Transactional
class GalleryDaoImpl extends GenericDaoImpl[Gallery, java.lang.Long] with GalleryDao
