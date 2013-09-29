package com.cloudray.scalapress.obj.attr

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class AttributeDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    test("persisting an attribute is assigned id") {

        val t = new Attribute
        t.name = "my type"
        t.attributeType = AttributeType.Text
        assert(t.id == 0)
        TestDatabaseContext.attributeDao.save(t)
        assert(t.id > 0)
    }

    test("persisting an attribute persists fields") {

        val t = new Attribute
        t.attributeType = AttributeType.Text
        t.name = "my type"
        TestDatabaseContext.attributeDao.save(t)
        val t2 = TestDatabaseContext.attributeDao.find(t.id)
        assert("my type" === t2.name)
    }

    test("persisting an attribute value is assigned id") {

        val t = new AttributeValue
        assert(t.id == 0)
        TestDatabaseContext.attributeValueDao.save(t)
        assert(t.id > 0)
    }

    test("persisting an attribute value persists fields") {

        val a = new Attribute
        a.name = "my type"
        a.attributeType = AttributeType.Text
        TestDatabaseContext.attributeDao.save(a)

        val av = new AttributeValue
        av.attribute = a
        av.value = "sups"
        TestDatabaseContext.attributeValueDao.save(av)

        val av2 = TestDatabaseContext.attributeValueDao.find(av.id)
        assert("sups" === av2.value)
        assert(av.attribute.id === av2.attribute.id)
    }
}
