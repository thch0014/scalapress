package com.liferay.scalapress.obj

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}

/** @author Stephen Samuel */
class ObjectExporterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val objectType = new ObjectType
    val attr1 = new Attribute
    attr1.name = "brand"
    attr1.id = 12

    val attr2 = new Attribute
    attr2.name = "code"
    attr2.id = 87

    val attributes = List(attr1, attr2)

    val obj = new Obj
    obj.name = "coldplay tickets"
    obj.id = 123
    obj.dateCreated = 1365722181000l

    val av1 = new AttributeValue
    av1.attribute = attr1
    av1.value = "Samsung"

    val av2 = new AttributeValue
    av2.attribute = attr2
    av2.value = "GalaxyS"

    obj.attributeValues.add(av1)
    obj.attributeValues.add(av2)

    test("header happy path") {

        val header = new ObjectExporter()._header(attributes)
        assert(Array("id", "date", "name", "url", "brand", "code") === header)
    }

    test("row happy path") {

        val header = new ObjectExporter()._row(obj, attributes, "mysite.com")
        assert(Array("123",
            "12-04-2013",
            "coldplay tickets",
            "http://mysite.com/object-123-coldplay-tickets",
            "Samsung",
            "GalaxyS") === header)
    }
}
