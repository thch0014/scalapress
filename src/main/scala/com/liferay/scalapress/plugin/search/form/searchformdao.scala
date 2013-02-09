package com.liferay.scalapress.plugin.search.form

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait SearchFormDao extends GenericDao[SearchForm, java.lang.Long]

@Component
@Transactional
class SearchFormDaoImpl extends GenericDaoImpl[SearchForm, java.lang.Long] with SearchFormDao

trait SearchFormFieldDao extends GenericDao[SearchFormField, java.lang.Long]

@Component
@Transactional
class SearchFormFieldDaoImpl extends GenericDaoImpl[SearchFormField, java.lang.Long] with SearchFormFieldDao