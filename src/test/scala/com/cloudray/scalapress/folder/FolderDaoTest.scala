package com.cloudray.scalapress.folder

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class FolderDaoTest extends FunSuite with MockitoSugar {

    val root = new Folder
    root.parent = null
    TestDatabaseContext.folderDao.save(root)

    val f1 = new Folder
    f1.parent = root
    f1.name = "a team"
    TestDatabaseContext.folderDao.save(f1)

    val f2 = new Folder
    f2.parent = f1
    f2.name = "hannibal"
    TestDatabaseContext.folderDao.save(f2)

    val f3 = new Folder
    f3.parent = root
    f3.name = "knight rider"
    TestDatabaseContext.folderDao.save(f3)

    test("persisting a folder is assigned id") {
        assert(f1.id > 0)
    }

    test("persisting a folder can be retrieved by id") {
        val f2 = TestDatabaseContext.folderDao.find(f1.id)
        assert("a team" === f2.name)
    }

    //    test("creating a new folder persists the new sections") {
    //
    //        val session = TestDatabaseContext.sf.openSession()
    //        session.setFlushMode(FlushMode.MANUAL)
    //        TransactionSynchronizationManager.bindResource(TestDatabaseContext.sf, new SessionHolder(session))
    //
    //        val f = Folder(root)
    //        TestDatabaseContext.folderDao.save(f)
    //
    //        assert(f.sections.size == 3)
    //        f.sections.asScala.foreach(section => assert(section.id > 0))
    //
    //        val f2 = TestDatabaseContext.folderDao.find(f.id)
    //        assert(f2.sections.size == 3)
    //        assert(f2.sections.asScala.find(_.isInstanceOf[FolderContentSection]).get.folder.id === f.id)
    //        assert(f2.sections.asScala.find(_.isInstanceOf[SubfolderSection]).get.folder.id === f.id)
    //        assert(f2.sections.asScala.find(_.isInstanceOf[ObjectListSection]).get.folder.id === f.id)
    //
    //        session.close()
    //    }

    //    test("root loads first folder without parent") {
    //        val actual = dao.root
    //        assert(root.id === actual.id)
    //    }
    //
    //    test("tree loads all folders") {
    //        val folders = dao.tree
    //        assert(4 === folders.size)
    //    }
    //    test("find top level loads all classes directly under root") {
    //        val folders = dao.findTopLevel
    //        assert(2 === folders.size)
    //        assert(!folders.exists(_.id == f2.id))
    //    }
}
