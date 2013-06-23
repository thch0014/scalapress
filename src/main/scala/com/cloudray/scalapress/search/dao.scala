package com.cloudray.scalapress.search

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */

trait SearchFormDao extends GenericDao[SearchForm, java.lang.Long]

@Component
@Transactional
class SearchFormDaoImpl extends GenericDaoImpl[SearchForm, java.lang.Long] with SearchFormDao

trait SearchFormFieldDao extends GenericDao[SearchFormField, java.lang.Long]

@Component
@Transactional
class SearchFormFieldDaoImpl extends GenericDaoImpl[SearchFormField, java.lang.Long] with SearchFormFieldDao

trait SavedSearchDao extends GenericDao[SavedSearch, java.lang.Long]

@Component
@Transactional
class SavedSearchDaoImpl extends GenericDaoImpl[SavedSearch, java.lang.Long] with SavedSearchDao

trait SearchSettingsDao extends GenericDao[SearchSettings, java.lang.Long] {
    def get: SearchSettings
}

@Component
@Transactional
class FolderPluginDaoImpl extends GenericDaoImpl[SearchSettings, java.lang.Long] with SearchSettingsDao {
    def get: SearchSettings = findAll.head
}