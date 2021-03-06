package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.csvreader.CsvReader
import com.cloudray.scalapress.item.attr.{Attribute, AttributeFuncs}
import com.cloudray.scalapress.folder.{Folder, FolderDao}

/** @author Stephen Samuel */
class ItemImporterTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val itemDao = mock[ItemDao]
  val folderDao = mock[FolderDao]
  val objectType = new ItemType
  val importer = new ItemImporter(itemDao, folderDao, objectType)
  val csv = mock[CsvReader]

  val obj = new Item
  obj.id = 14

  test("importer sets name on item") {
    Mockito.when(csv.get("name")).thenReturn("sammy")
    importer.setValues(obj, csv)
    assert("sammy" === obj.name)
  }

  test("importer sets status on item") {
    Mockito.when(csv.get("status")).thenReturn(Item.STATUS_DELETED)
    importer.setValues(obj, csv)
    assert(Item.STATUS_DELETED === obj.status)
  }

  test("importer defaults to live if status not known") {
    Mockito.when(csv.get("status")).thenReturn("bollocks")
    importer.setValues(obj, csv)
    assert(Item.STATUS_LIVE === obj.status)
  }

  test("importer sets price on item") {
    Mockito.when(csv.get("price")).thenReturn("45.56")
    importer.setValues(obj, csv)
    assert(4556 === obj.price)
  }

  test("importer sets cost price on item") {
    Mockito.when(csv.get("cost")).thenReturn("34.88")
    importer.setValues(obj, csv)
    assert(3488 === obj.costPrice)
  }

  test("importer clears existing folders") {
    obj.folders.add(new Folder)
    importer.setValues(obj, csv)
    assert(obj.folders.isEmpty)
  }

  test("importer adds new folders") {
    Mockito.when(csv.get("folders")).thenReturn("3|4")
    val folder3 = new Folder
    folder3.id = 3
    folder3.name = "boro"
    val folder4 = new Folder
    folder4.id = 4
    folder4.name = "toon"
    Mockito.when(folderDao.find(3)).thenReturn(folder3)
    Mockito.when(folderDao.find(4)).thenReturn(folder4)
    importer.setValues(obj, csv)
    assert(2 === obj.folders.size)
  }

  test("importer sets rrp on item") {
    Mockito.when(csv.get("rrp")).thenReturn("19.96")
    importer.setValues(obj, csv)
    assert(1996 === obj.rrp)
  }

  test("importer sets stock on item") {
    Mockito.when(csv.get("stock")).thenReturn("456")
    importer.setValues(obj, csv)
    assert(456 === obj.stock)
  }

  test("importer processes CSV file and sets values") {
    val obj = new Item
    obj.id = 12
    Mockito.when(itemDao.find(12)).thenReturn(obj)
    val string = "id,name,status,price\n12,coldplay tickets,live,29.99"
    importer.doImport(string)
    assert(12 === obj.id)
    assert("coldplay tickets" === obj.name)
    assert("Live" === obj.status)
    assert(2999 === obj.price)
  }

  test("importer processes CSV file and sets attribute values") {

    val a = new Attribute
    a.name = "brand"

    objectType.attributes.add(a)

    val obj = new Item
    obj.id = 12
    Mockito.when(itemDao.find(12)).thenReturn(obj)
    val string = "id,name,status,brand\n12,galaxy s4,live,samsung"
    importer.doImport(string)
    assert(12 === obj.id)
    assert("samsung" === AttributeFuncs.attributeValue(obj, "brand").get)
  }

  test("importer persists objects") {
    val obj = new Item
    obj.id = 12
    Mockito.when(itemDao.find(12)).thenReturn(obj)
    val string = "id,name,status,price\n12,coldplay tickets,live,29.99"
    importer.doImport(string)
    Mockito.verify(itemDao).save(obj)
  }

  test("importer does not change objects not included in the file") {

    val obj = new Item
    obj.id = 12
    Mockito.when(itemDao.find(12)).thenReturn(obj)

    val obj2 = new Item
    obj2.id = 55
    obj2.status = "boo"
    obj2.name = "super name"
    Mockito.when(itemDao.find(55)).thenReturn(obj2)

    val string = "id,name,status,price\n12,coldplay tickets,live,29.99"
    importer.doImport(string)

    assert(55 === obj2.id)
    assert("super name" === obj2.name)
    assert("boo" === obj2.status)

    Mockito.verify(itemDao, Mockito.never).save(obj2)
  }
}
