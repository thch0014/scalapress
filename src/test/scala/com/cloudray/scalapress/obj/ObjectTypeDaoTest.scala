package com.cloudray.scalapress.obj

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class ObjectTypeDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[TypeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[TypeDao]

    test("persisting an obj can be retrieved by id") {

        val t = new ObjectType
        t.name = "my type"

        assert(t.id == null)
        dao.save(t)
        assert(t.id > 0)

        val t2 = dao.find(t.id)
        assert("my type" === t2.name)
    }
}
