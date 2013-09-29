package com.cloudray.scalapress.plugin.form

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import FormFieldType
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class FormDaoTest extends FunSuite with MockitoSugar {

    test("persisting a form sets id and fields") {

        val t = new Form
        t.name = "fornicating form"

        assert(t.id === 0)
        TestDatabaseContext.formDao.save(t)
        assert(t.id > 0)

        val t2 = TestDatabaseContext.formDao.find(t.id)
        assert("fornicating form" === t2.name)
    }

    test("persisting a form field sets id and fields") {

        val m = new FormField
        m.name = "western"
        m.fieldType = FormFieldType.Email
        m.regExp = "*.*.*.{2,}"

        assert(m.id === 0)
        TestDatabaseContext.formFieldDao.save(m)
        assert(m.id > 0)

        val m2 = TestDatabaseContext.formFieldDao.find(m.id)
        assert("western" === m2.name)
        assert(FormFieldType.Email === m2.fieldType)
        assert("*.*.*.{2,}" === m2.regExp)
    }
}
