package com.cloudray.scalapress.plugin.feed.gbase

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import com.cloudray.scalapress.obj.{Obj, ObjectDao, ObjectDaoImpl}
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class GoogleBaseServiceTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[ObjectDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[ObjectDao]

    test("that objects can be retrieved") {

        val obj1 = new Obj
        obj1.name = "grandfather"
        obj1.status = Obj.STATUS_LIVE
        obj1.price = 10

        val obj2 = new Obj
        obj2.name = "father"
        obj2.status = Obj.STATUS_DELETED
        obj2.price = 12

        val obj3 = new Obj
        obj3.name = "son"
        obj3.status = Obj.STATUS_LIVE
        obj3.price = 0

        dao.save(obj1)
        dao.save(obj2)
        dao.save(obj3)

        val objs = GoogleBaseService._objects(dao)
        assert(objs.exists(_.id == obj1.id))
        assert(!objs.exists(_.id == obj2.id))
        assert(!objs.exists(_.id == obj3.id))
    }
}
