package com.cloudray.scalapress.folder

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.folder.{Folder, FolderDaoImpl, FolderDao}

/** @author Stephen Samuel */
class FolderDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[FolderDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[FolderDao]

    val root = new Folder
    root.parent = null
    dao.save(root)

    test("persisting a folder is assigned id") {

        val f = new Folder
        f.parent = root
        assert(f.id == 0)
        dao.save(f)
        assert(f.id > 0)
    }

    test("persisting a folder can be retrieved by id") {

        val f = new Folder
        f.parent = root
        f.name = "fold it up"
        dao.save(f)

        val f2 = dao.find(f.id)
        assert("fold it up" === f2.name)
    }

    test("root loads first folder without parent") {
        // should be 3 at this point in the test run - dependant on order that tests run so be careful if moving shit about
        val actual = dao.root
        assert(root.id === actual.id)
    }

    test("tree loads all folders starting with root") {
        // should be 3 at this point in the test run - dependant on order that tests run so be careful if moving shit about
        val folders = dao.tree
        assert(3 === folders.size)
    }

    test("find top level loads all classes directly under root") {

        val toplevel3 = new Folder
        toplevel3.parent = root
        dao.save(toplevel3)

        val nottoplevel = new Folder
        nottoplevel.parent = toplevel3
        dao.save(nottoplevel)

        // should be 3 top level at this point - 2 from previous tests and 1 created here, nottoplevel as a non root parent
        val folders = dao.findTopLevel
        assert(3 === folders.size)
        assert(!folders.exists(_.id == nottoplevel.id))
    }
}
