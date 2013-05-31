package com.cloudray.scalapress.plugin.form

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.enums.FormFieldType

/** @author Stephen Samuel */
class FormDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val formDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[FormDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[FormDao]

    val formFieldDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[FormFieldDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[FormFieldDao]

    test("persisting a form sets id and fields") {

        val t = new Form
        t.name = "fornicating form"

        assert(t.id === 0)
        formDao.save(t)
        assert(t.id > 0)

        val t2 = formDao.find(t.id)
        assert("fornicating form" === t2.name)
    }

    test("persisting a form field sets id and fields") {

        val m = new FormField
        m.name = "western"
        m.fieldType = FormFieldType.Email
        m.regExp = "*.*.*.{2,}"

        assert(m.id === 0)
        formFieldDao.save(m)
        assert(m.id > 0)

        val m2 = formFieldDao.find(m.id)
        assert("western" === m2.name)
        assert(FormFieldType.Email === m2.fieldType)
        assert("*.*.*.{2,}" === m2.regExp)
    }
}
