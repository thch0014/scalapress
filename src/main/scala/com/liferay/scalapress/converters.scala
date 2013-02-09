package com.liferay.scalapress

import com.liferay.scalapress.dao.{MarkupDao, TypeDao, FolderDao}
import org.springframework.core.convert.converter.Converter
import com.liferay.scalapress.domain.{Markup, ObjectType, Folder}
import plugin.search.form.{SearchForm, SearchFormDao}

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