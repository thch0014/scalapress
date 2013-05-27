package com.liferay.scalapress

import folder.FolderDao
import obj.TypeDao
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import plugin.ecommerce.dao.{AddressDao, DeliveryOptionDao}
import plugin.form.FormDao
import search.SearchFormDao
import theme.{ThemeDao, MarkupDao}
import com.liferay.scalapress.obj.attr.AttributeDao

/** @author Stephen Samuel */
class ConvertersTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("string to folder happy path") {
        val folderDao = mock[FolderDao]
        new StringFolderConverter(folderDao).convert("2")
        Mockito.verify(folderDao).find(2)
    }

    test("string to object type happy path") {
        val objectTypeDao = mock[TypeDao]
        new StringObjectTypeConverter(objectTypeDao).convert("2")
        Mockito.verify(objectTypeDao).find(2)
    }

    test("string to attribute happy path") {
        val attributeDao = mock[AttributeDao]
        new StringToAttributeConvertor(attributeDao).convert("2")
        Mockito.verify(attributeDao).find(2)
    }

    test("string to markup type happy path") {
        val markupDao = mock[MarkupDao]
        new StringMarkupConverter(markupDao).convert("2")
        Mockito.verify(markupDao).find(2)
    }

    test("string to search form happy path") {
        val searchFormDao = mock[SearchFormDao]
        new StringSearchFormConverter(searchFormDao).convert("2")
        Mockito.verify(searchFormDao).find(2)
    }

    test("string to theme type happy path") {
        val themeDao = mock[ThemeDao]
        new StringToThemeConverter(themeDao).convert("2")
        Mockito.verify(themeDao).find(2)
    }

    test("string to del option happy path") {
        val deliveryOptionDao = mock[DeliveryOptionDao]
        new StringDeliveryOptionConverter(deliveryOptionDao).convert("2")
        Mockito.verify(deliveryOptionDao).find(2)
    }

    test("string to form type happy path") {
        val formDao = mock[FormDao]
        new StringFormConverter(formDao).convert("2")
        Mockito.verify(formDao).find(2)
    }

    test("string to address happy path") {
        val addressDao = mock[AddressDao]
        new StringToAddressConverter(addressDao).convert("2")
        Mockito.verify(addressDao).find(2)
    }
}
