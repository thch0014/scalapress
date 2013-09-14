package com.cloudray.scalapress.folder

import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class FolderDaoImplTest extends FlatSpec with MockitoSugar {

  val f1 = new Folder
  f1.name = "teams"

  val f2 = new Folder
  f2.parent = f1
  f1.subfolders.add(f2)
  f2.name = "boro"

  val f3 = new Folder
  f3.parent = f1
  f1.subfolders.add(f3)
  f3.name = "sunderland"

  val f4 = new Folder
  f4.parent = f2
  f2.subfolders.add(f4)
  f4.name = "barcodes"

  TestDatabaseContext.folderDao.save(f1)
  TestDatabaseContext.folderDao.save(f2)
  TestDatabaseContext.folderDao.save(f3)
  TestDatabaseContext.folderDao.save(f4)

//  "a folder dao" should "find top level" in {
//    val toplevel = TestDatabaseContext.folderDao.findTopLevel
//    assert(2 === toplevel.size)
//    assert(toplevel.exists(_.id == f2.id))
//    assert(toplevel.exists(_.id == f3.id))
//  }
//
//  it should "find all nodes in tree" in {
//    val tree = TestDatabaseContext.folderDao.tree
//    assert(4 === tree.size)
//  }
//
//  it should "find single root" in {
//    val root = TestDatabaseContext.folderDao.root
//    assert(root.id == f1.id)
//  }
//
//  it should "assign ids to persisted folders" in {
//    assert(f1.id > 0)
//  }
//
//  it should "retrieve a fo++lder by id" in {
//    val f2 = TestDatabaseContext.folderDao.find(f1.id)
//    assert("teams" === f2.name)
//  }
}
