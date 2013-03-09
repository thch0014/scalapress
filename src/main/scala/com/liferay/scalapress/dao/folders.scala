package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.hibernate.criterion.Restrictions
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.domain.Folder
import com.liferay.scalapress.plugin.FolderPlugin
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
trait FolderDao extends GenericDao[Folder, java.lang.Long] {
    def findTopLevel: Array[Folder]
    def root: Folder
    def search(query: String): Array[Folder]
}

@Component
@Transactional
class FolderDaoImpl extends GenericDaoImpl[Folder, java.lang.Long] with FolderDao {

    override def findTopLevel: Array[Folder] = root
      .subfolders
      .asScala
      .toArray

    @Transactional
    override def root: Folder = {
        getSession
          .createCriteria(classOf[Folder])
          .add(Restrictions.isNull("parent"))
          .uniqueResult.asInstanceOf[Folder]
    }

    override def findAll: List[Folder] = super.findAll.sortWith((a, b) => a.name < b.name)
    @Transactional
    override def search(query: String): Array[Folder] = {
        getSession
          .createCriteria(classOf[Folder])
          .add(Restrictions.like("name", "%" + query))
          .setMaxResults(20)
          .list()
          .asScala
          .map(_.asInstanceOf[Folder])
          .toArray
    }
}

trait FolderPluginDao extends GenericDao[FolderPlugin, java.lang.Long] {
    def head: FolderPlugin
}

@Component
@Transactional
class FolderPluginDaoImpl extends GenericDaoImpl[FolderPlugin, java.lang.Long] with FolderPluginDao {
    def head: FolderPlugin = findAll.head
}

@Component
class FolderPluginDaoValidator {
    @Autowired var folderPluginDao: FolderPluginDao = _
    @PostConstruct def ensureOne() {
        if (folderPluginDao.findAll().size == 0) {
            val plugin = new FolderPlugin
            folderPluginDao.save(plugin)
        }
    }
}

