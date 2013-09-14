package com.cloudray.scalapress.folder

import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.hibernate.criterion.Restrictions
import com.googlecode.genericdao.search.ISearch

/** @author Stephen Samuel */
trait FolderDao extends GenericDao[Folder, java.lang.Long] {
  def findTopLevel: Array[Folder]
  def root: Folder
  def tree: Array[Folder]
  def exposeSearch[T](search: ISearch): Seq[T]
}

@Component
@Transactional
class FolderDaoImpl extends GenericDaoImpl[Folder, java.lang.Long] with FolderDao {

  def exposeSearch[T](search: ISearch): Seq[T] = super._search(search).asInstanceOf[java.util.List[T]].asScala

  override def tree: Array[Folder] = _tree(root).toArray

  def _tree(parent: Folder): ArrayBuffer[Folder] = {
    val buffer = new ArrayBuffer[Folder]
    buffer.append(parent)
    for ( child <- parent.subfolders.asScala )
      buffer.appendAll(_tree(child))
    buffer
  }

  override def findTopLevel: Array[Folder] = root.subfolders.asScala.toArray

  override def root: Folder = {
    val c = getSession.createCriteria(classOf[Folder]).add(Restrictions.isNull("parent"))
    c.list().asScala.head.asInstanceOf[Folder]
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

