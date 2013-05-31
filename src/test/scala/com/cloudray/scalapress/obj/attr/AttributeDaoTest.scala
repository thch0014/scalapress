package com.cloudray.scalapress.obj.attr

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class AttributeDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val attributeDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[AttributeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[AttributeDao]

    val attributeValueDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[AttributeValueDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[AttributeValueDao]

    test("persisting an attribute is assigned id") {

        val t = new Attribute
        t.name = "my type"
        t.attributeType = AttributeType.Text
        assert(t.id == 0)
        attributeDao.save(t)
        assert(t.id > 0)
    }

    test("persisting an attribute persists fields") {

        val t = new Attribute
        t.attributeType = AttributeType.Text
        t.name = "my type"
        attributeDao.save(t)
        val t2 = attributeDao.find(t.id)
        assert("my type" === t2.name)
    }

    test("persisting an attribute value is assigned id") {

        val t = new AttributeValue
        assert(t.id == 0)
        attributeValueDao.save(t)
        assert(t.id > 0)
    }

    test("persisting an attribute value persists fields") {

        val a = new Attribute
        a.name = "my type"
        a.attributeType = AttributeType.Text
        attributeDao.save(a)

        val av = new AttributeValue
        av.attribute = a
        av.value = "sups"
        attributeValueDao.save(av)

        val av2 = attributeValueDao.find(av.id)
        assert("sups" === av2.value)
        assert(av.attribute.id === av2.attribute.id)
    }
}
