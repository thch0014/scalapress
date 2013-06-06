package com.cloudray.scalapress.obj

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class ObjDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[ObjectDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[ObjectDao]

    test("persisting an obj is assigned id") {

        val obj = new Obj
        assert(obj.id == 0)
        dao.save(obj)
        assert(obj.id > 0)
    }

    test("persisting an obj can be retrieved by id") {

        val obj = new Obj
        obj.name = "super obj"
        assert(obj.id == 0)
        dao.save(obj)
        assert(obj.id > 0)
        val obj2 = dao.find(obj.id)
        assert("super obj" === obj2.name)
    }

    test("objects can be loaded by bulk using ids") {

        val obj1 = new Obj
        obj1.name = "coldplay"

        val obj2 = new Obj
        obj2.name = "playcold"

        dao.save(obj1)
        dao.save(obj2)

        val objects = dao.findBulk(Seq(obj1.id, obj2.id))
        assert(objects.exists(_.id == obj1.id))
        assert(objects.exists("coldplay" == _.name))
        assert(objects.exists(_.id == obj2.id))
        assert(objects.exists("playcold" == _.name))
    }

    test("recent returns X results in newest id order") {

        val obj1 = new Obj
        obj1.name = "grandfather"
        obj1.status = Obj.STATUS_LIVE

        val obj2 = new Obj
        obj2.name = "father"
        obj2.status = Obj.STATUS_LIVE

        val obj3 = new Obj
        obj3.name = "son"
        obj3.status = Obj.STATUS_LIVE

        dao.save(obj1)
        dao.save(obj2)
        dao.save(obj3)

        val objs = dao.recent(2)
        assert(2 === objs.size)
        assert(obj3.id === objs(0).id)
        assert(obj2.id === objs(1).id)
    }


    test("recent only includes live objects") {

        val obj1 = new Obj
        obj1.name = "grandfather"
        obj1.status = Obj.STATUS_DELETED

        val obj2 = new Obj
        obj2.name = "father"
        obj2.status = Obj.STATUS_DISABLED

        val obj3 = new Obj
        obj3.name = "son"
        obj3.status = Obj.STATUS_LIVE

        dao.save(obj1)
        dao.save(obj2)
        dao.save(obj3)

        val objs = dao.recent(5)
        assert(4 === objs.size)
        assert(obj3.id === objs(0).id)
    }
}
