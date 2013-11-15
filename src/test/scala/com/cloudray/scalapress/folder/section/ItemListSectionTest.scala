package com.cloudray.scalapress.folder.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{FolderSettings, FolderPluginDao, Folder}
import java.util
import com.cloudray.scalapress.item.{ItemType, Item}
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.item.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.theme.Markup
import com.cloudray.scalapress.search.Sort
import com.cloudray.scalapress.framework.{ScalapressConstants, ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class ItemListSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val obj1 = new Item
  obj1.name = "coldplay"
  obj1.status = "live"
  obj1.id = 76
  obj1.itemType = new ItemType
  obj1.itemType.id = 1

  val obj2 = new Item
  obj2.name = "jethro tull"
  obj2.status = "disabled"
  obj2.id = 25
  obj2.itemType = obj1.itemType

  val obj3 = new Item
  obj3.name = "keane"
  obj3.status = "live"
  obj3.id = 11
  obj3.itemType = obj1.itemType

  val section = new ItemListSection()
  section.folder = new Folder
  section.markup = new Markup
  section.folder.objects = new util.HashSet()

  section.folder.objects.add(obj1)
  section.folder.objects.add(obj2)
  section.folder.objects.add(obj3)

  val req = mock[HttpServletRequest]
  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://domain.com"))
  val context = new ScalapressContext()
  val sreq = ScalapressRequest(req, context)
  Mockito.doReturn("abc-ses").when(req).getAttribute(ScalapressConstants.RequestAttributeKey_SessionId)

  val settings = new FolderSettings
  context.folderSettingsDao = mock[FolderPluginDao]
  Mockito.when(context.folderSettingsDao.head).thenReturn(settings)

  test("only live object are included") {
    val objects = section._objects(sreq)
    assert(0 === objects.count(_.status.toLowerCase != "live"))
    assert(2 === objects.size)
  }

  test("name sort happy path") {
    section.sort = Sort.Name
    val objects = section._objects(sreq)
    assert(objects(0) === obj1)
    assert(objects(1) === obj3)
  }

  test("newest sort happy path") {
    section.sort = Sort.Newest
    val objects = section._objects(sreq)
    assert(objects(0) === obj1)
    assert(objects(1) === obj3)
  }

  test("oldest sort happy path") {
    section.sort = Sort.Oldest
    val objects = section._objects(sreq)
    assert(objects(0) === obj3)
    assert(objects(1) === obj1)
  }

  test("pagination is included if objects > pageSize") {
    section.pageSize = 1
    val render = section.render(sreq).get
    assert(render.contains("pagination"))
  }

  test("pagination is not included if objects <= pageSize") {
    section.pageSize = 11
    val render = section.render(sreq).get
    assert(!render.contains("pagination"))
  }

  test("page size uses default from folder settings if not specified") {
    section.pageSize = 0
    settings.pageSize = 666
    val pageSize = section._pageSize(context)
    assert(666 === pageSize)
  }

  test("folder settings page size is not used if pagesize>0") {
    section.pageSize = 5
    settings.pageSize = 666
    val pageSize = section._pageSize(context)
    assert(5 === pageSize)
  }

  test("object list page size default is used if neither folder settings nor section specify a page size") {
    section.pageSize = 0
    settings.pageSize = 0
    val pageSize = section._pageSize(context)
    assert(ItemListSection.PAGE_SIZE_DEFAULT === pageSize)
  }

  test("section uses sort from folder settings if not specified in section") {
    section.sort = null
    settings.sort = Sort.Oldest
    val sort = section._sort(context)
    assert(Sort.Oldest === sort)
  }

  test("folder settings sort is not used if sort from folder is not null") {
    section.sort = Sort.Price
    settings.sort = Sort.Oldest
    val sort = section._sort(context)
    assert(Sort.Price === sort)
  }

  test("sorting by attribute handles empty attribute values") {
    section.sort = Sort.Attribute
    section.sortAttribute = new Attribute

    val av1 = new AttributeValue
    av1.attribute = section.sortAttribute
    av1.value = "jethro tull"
    obj1.attributeValues.add(av1)

    val av2 = new AttributeValue
    av2.attribute = section.sortAttribute
    av2.value = "coldplay"
    obj2.attributeValues.add(av2)
    obj2.status = "live"

    val av3 = new AttributeValue
    av3.attribute = section.sortAttribute
    obj3.attributeValues.add(av3)

    val objects = section._objects(sreq)
    assert(objects(0) === obj3)
    assert(objects(1) === obj2)
    assert(objects(2) === obj1)
  }

  test("sorting by attribute reverse handles empty attribute values") {
    section.sort = Sort.AttributeDesc
    section.sortAttribute = new Attribute

    val av1 = new AttributeValue
    av1.attribute = section.sortAttribute
    av1.value = "jethro tull"
    obj1.attributeValues.add(av1)

    val av2 = new AttributeValue
    av2.attribute = section.sortAttribute
    av2.value = "coldplay"
    obj2.status = "live"
    obj2.attributeValues.add(av2)

    val av3 = new AttributeValue
    av3.attribute = section.sortAttribute
    obj3.attributeValues.add(av3)

    val objects = section._objects(sreq)
    assert(objects(0) === obj1)
    assert(objects(1) === obj2)
    assert(objects(2) === obj3)
  }
}
