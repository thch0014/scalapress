package com.cloudray.scalapress

import folder.{FolderDao, Folder}
import com.cloudray.scalapress.item.attr.{AttributeDao, Attribute}
import item.{TypeDao, ItemType}
import org.springframework.core.convert.converter.Converter
import plugin.ecommerce.domain.{Address, DeliveryOption}
import plugin.form.{Form, FormDao}
import search.{SearchFormDao, SearchForm}
import theme.{Theme, Markup, MarkupDao, ThemeDao}
import com.cloudray.scalapress.plugin.gallery.base.{Gallery, GalleryDao}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{AddressDao, DeliveryOptionDao}

/** @author Stephen Samuel */
class StringFolderConverter(folderDao: FolderDao) extends Converter[String, Folder] {
  def convert(source: String): Folder =
    if (source == null || source == "" || source == "0") null
    else folderDao.find(source.toLong)
}

class StringItemTypeConverter(objectTypeDao: TypeDao) extends Converter[String, ItemType] {
  def convert(source: String): ItemType =
    if (source == null || source.trim.isEmpty || source == "0") null
    else objectTypeDao.find(source.toLong)
}

class StringToGalleryConverter(galleryDao: GalleryDao) extends Converter[String, Gallery] {
  def convert(source: String): Gallery =
    if (source == null || source.trim.isEmpty || source == "0") null
    else galleryDao.find(source.toLong)
}

class StringToAttributeConvertor(attributeDao: AttributeDao) extends Converter[String, Attribute] {
  def convert(source: String): Attribute =
    if (source == null || source == "" || source == "0") null
    else attributeDao.find(source.toLong)
}

class StringFormConverter(formDao: FormDao) extends Converter[String, Form] {
  def convert(source: String): Form =
    if (source == null || source == "" || source == "0") null
    else formDao.find(source.toLong)
}

class StringMarkupConverter(markupDao: MarkupDao) extends Converter[String, Markup] {
  def convert(source: String): Markup =
    if (source == null || source == "" || source == "0") null
    else markupDao.find(source.toLong)
}

class StringSearchFormConverter(searchFormDao: SearchFormDao) extends Converter[String, SearchForm] {
  def convert(source: String): SearchForm =
    if (source == null || source == "" || source == "0") null
    else searchFormDao.find(source.toLong)
}

class StringDeliveryOptionConverter(deliveryOptionDao: DeliveryOptionDao) extends Converter[String, DeliveryOption] {
  def convert(source: String): DeliveryOption =
    if (source == null || source == "" || source == "0") null
    else deliveryOptionDao.find(source.toLong)
}

class StringToThemeConverter(themeDao: ThemeDao) extends Converter[String, Theme] {
  def convert(source: String): Theme =
    if (source == null || source == "" || source == "0") null
    else themeDao.find(source.toLong)
}

class StringToAddressConverter(addressDao: AddressDao) extends Converter[String, Address] {
  def convert(source: String): Address =
    if (source == null || source == "" || source == "0") null
    else addressDao.find(source.toLong)
}
