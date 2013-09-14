package com.cloudray.scalapress.folder

import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class FolderDaoImplTest extends FlatSpec with MockitoSugar {

  val f1 = new Folder
  val f2 = new Folder
  f2.parent = f1
  val f3 = new Folder
  f3.parent = f1
  val f4 = new Folder
  f4.parent = f3

  TestDatabaseContext.folderDao.save(f1)
  TestDatabaseContext.folderDao.save(f2)
  TestDatabaseContext.folderDao.save(f3)
  TestDatabaseContext.folderDao.save(f4)

  "a folder dao" should "find top level" in {
    val toplevel = TestDatabaseContext.folderDao.findTopLevel
    assert(2 === toplevel.size)
    assert(toplevel.exists(_.id == f2.id))
    assert(toplevel.exists(_.id == f3.id))
  }

  "a folder dao" should "find all nodes in tree" in {
    val tree = TestDatabaseContext.folderDao.tree
    println(tree.toSeq)
    assert(4 === tree.size)
  }

  "a folder dao" should "find single root" in {
    val root = TestDatabaseContext.folderDao.root
    assert(root.id == f1.id)
  }
}
