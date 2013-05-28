package com.cloudray.scalapress.folder

import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.hibernate.criterion.Restrictions

/** @author Stephen Samuel */
trait FolderDao extends GenericDao[Folder, java.lang.Long] {
    def findTopLevel: Array[Folder]
    def root: Folder
    def tree: Array[Folder]
}

@Component
@Transactional
class FolderDaoImpl extends GenericDaoImpl[Folder, java.lang.Long] with FolderDao {

    override def tree: Array[Folder] = {
        tree(root).toArray
    }

    private def tree(parent: Folder): ArrayBuffer[Folder] = {
        val buffer = new ArrayBuffer[Folder]
        buffer.append(parent)
        for ( child <- parent.subfolders.asScala )
            buffer.appendAll(tree(child))
        buffer
    }

    override def findTopLevel: Array[Folder] = root.subfolders.asScala.toArray

    override def root: Folder = {
        val c = getSession.createCriteria(classOf[Folder]).add(Restrictions.isNull("parent"))
        c.setCacheable(true)
        c.list().asScala.head.asInstanceOf[Folder]
    }

    override def findAll: List[Folder] = {
        val c = getSession.createCriteria(classOf[Folder])
        c.setCacheable(true)
        val results = c.list.asInstanceOf[java.util.List[Folder]]
        results.asScala.sortWith((a, b) => a.name < b.name).toList
    }
}

trait FolderPluginDao extends GenericDao[FolderSettings, java.lang.Long] {
    def head: FolderSettings
}

@Component
@Transactional
class FolderPluginDaoImpl extends GenericDaoImpl[FolderSettings, java.lang.Long] with FolderPluginDao {
    def head: FolderSettings = findAll.head
}

@Component
class FoldersValidator {

    @Autowired var folderPluginDao: FolderPluginDao = _
    @Autowired var folderDao: FolderDao = _

    @PostConstruct def ensureOnePlugin() {
        if (folderPluginDao.findAll().size == 0) {
            val plugin = new FolderSettings
            folderPluginDao.save(plugin)
        }
    }

    @PostConstruct def ensureRoot() {
        if (folderDao.findAll().size == 0) {
            val root = Folder(null)
            root.name = "Home Page"
            folderDao.save(root)
        }
    }
}

