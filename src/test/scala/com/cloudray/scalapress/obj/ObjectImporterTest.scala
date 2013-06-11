package com.cloudray.scalapress.obj

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.csvreader.CsvReader
import com.cloudray.scalapress.obj.attr.{Attribute, AttributeFuncs}

/** @author Stephen Samuel */
class ObjectImporterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val objectDao = mock[ObjectDao]
    val objectType = new ObjectType
    val importer = new ObjectImporter(objectDao, objectType)
    val csv = mock[CsvReader]

    val obj = new Obj
    obj.id = 14

    test("importer sets name on obj") {
        Mockito.when(csv.get("name")).thenReturn("sammy")
        importer.setValues(obj, csv)
        assert("sammy" === obj.name)
    }

    test("importer sets status on obj") {
        Mockito.when(csv.get("status")).thenReturn(Obj.STATUS_DELETED)
        importer.setValues(obj, csv)
        assert(Obj.STATUS_DELETED === obj.status)
    }

    test("importer sets price on obj") {
        Mockito.when(csv.get("price")).thenReturn("45.56")
        importer.setValues(obj, csv)
        assert(4556 === obj.price)
    }

    test("importer sets cost price on obj") {
        Mockito.when(csv.get("cost")).thenReturn("34.88")
        importer.setValues(obj, csv)
        assert(3488 === obj.costPrice)
    }

    test("importer sets rrp on obj") {
        Mockito.when(csv.get("rrp")).thenReturn("19.96")
        importer.setValues(obj, csv)
        assert(1996 === obj.rrp)
    }

    test("importer sets stock on obj") {
        Mockito.when(csv.get("stock")).thenReturn("456")
        importer.setValues(obj, csv)
        assert(456 === obj.stock)
    }

    test("importer processes CSV file and sets values") {
        val obj = new Obj
        obj.id = 12
        Mockito.when(objectDao.find(12)).thenReturn(obj)
        val string = "id,name,status,price\n12,coldplay tickets,live,29.99"
        importer.doImport(string)
        assert(12 === obj.id)
        assert("coldplay tickets" === obj.name)
        assert("live" === obj.status)
        assert(2999 === obj.price)
    }

    test("importer processes CSV file and sets attribute values") {

        val a = new Attribute
        a.name = "brand"

        objectType.attributes.add(a)

        val obj = new Obj
        obj.id = 12
        Mockito.when(objectDao.find(12)).thenReturn(obj)
        val string = "id,name,status,brand\n12,galaxy s4,live,samsung"
        importer.doImport(string)
        assert(12 === obj.id)
        assert("samsung" === AttributeFuncs.attributeValue(obj, "brand").get)
    }
}
