package com.cloudray.scalapress.folder

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

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

    val f1 = new Folder
    f1.parent = root
    f1.name = "a team"
    dao.save(f1)

    val f2 = new Folder
    f2.parent = f1
    f2.name = "hannibal"
    dao.save(f2)

    val f3 = new Folder
    f3.parent = root
    f3.name = "knight rider"
    dao.save(f3)

    test("persisting a folder is assigned id") {
        assert(f1.id > 0)
    }

    test("persisting a folder can be retrieved by id") {
        val f2 = dao.find(f1.id)
        assert("a team" === f2.name)
    }

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
