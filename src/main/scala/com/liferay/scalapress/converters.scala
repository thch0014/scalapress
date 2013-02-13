package com.liferay.scalapress

import dao.{ThemeDao, MarkupDao, TypeDao, FolderDao}
import domain.setup.Theme
import org.springframework.core.convert.converter.Converter
import com.liferay.scalapress.domain.{Markup, ObjectType, Folder}
import plugin.ecommerce.dao.DeliveryOptionDao
import plugin.ecommerce.domain.DeliveryOption
import plugin.search.form.SearchForm
import plugin.search.SearchFormDao

/** @author Stephen Samuel */
class StringFolderConverter(folderDao: FolderDao) extends Converter[String, Folder] {
    def convert(source: String): Folder =
        if (source == null || source == "" || source == "0") null
        else folderDao.find(source.toInt)
}

class StringObjectTypeConverter(objectTypeDao: TypeDao) extends Converter[String, ObjectType] {
    def convert(source: String): ObjectType =
        if (source == null || source == "" || source == "0") null
        else objectTypeDao.find(source.toInt)
}

class StringMarkupConverter(markupDao: MarkupDao) extends Converter[String, Markup] {
    def convert(source: String): Markup =
        if (source == null || source == "" || source == "0") null
        else markupDao.find(source.toInt)
}

class StringSearchFormConverter(searchFormDao: SearchFormDao) extends Converter[String, SearchForm] {
    def convert(source: String): SearchForm =
        if (source == null || source == "" || source == "0") null
        else searchFormDao.find(source.toInt)
}

class StringDeliveryOptionConverter(deliveryOptionDao: DeliveryOptionDao) extends Converter[String, DeliveryOption] {
    def convert(source: String): DeliveryOption =
        if (source == null || source == "" || source == "0") null
        else deliveryOptionDao.find(source.toInt)
}

class StringToThemeConverter(themeDao: ThemeDao) extends Converter[String, Theme] {
    def convert(source: String): Theme =
        if (source == null || source == "" || source == "0") null
        else themeDao.find(source.toInt)
}
