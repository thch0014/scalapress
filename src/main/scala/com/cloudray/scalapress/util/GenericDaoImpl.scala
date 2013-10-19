package com.cloudray.scalapress.util

import com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO
import com.googlecode.genericdao.search.{SearchResult, ISearch, Search}
import com.googlecode.genericdao.dao.DAOUtil
import scala.collection.JavaConverters._
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory

/** @author Stephen Samuel */
/**
 * Implementation of <code>GenericDAO</code> using Hibernate.
 * The SessionFactory property is annotated for automatic resource injection.
 *
 */
@Transactional
class GenericDaoImpl[T <: AnyRef, ID <: java.io.Serializable] extends HibernateBaseDAO with GenericDao[T, ID] {

  @Autowired
  override def setSessionFactory(sessionFactory: SessionFactory) {
    super.setSessionFactory(sessionFactory)
  }

  protected var persistentClass: Class[T] = DAOUtil
    .getTypeArguments(classOf[GenericDaoImpl[_, _]], this.getClass)
    .get(0)
    .asInstanceOf[Class[T]]

  def count: Int = _count(persistentClass, new Search)

  def count(search: ISearch): Int = {
    _count(persistentClass, search)
  }

  def find(id: ID): T = {
    _get(persistentClass, id)
  }

  def findAll: List[T] = {
    _all(persistentClass).asScala.toList
  }

  def update(entity: T) = super._saveOrUpdate(entity)

  def merge(entity: T) = super._merge(entity)

  def findAll(limit: Int): List[T] = {
    search(new Search(persistentClass).setMaxResults(limit))
  }

  def remove(entity: T): Boolean = {
    _deleteEntity(entity)
  }

  def removeById(id: ID): Boolean = {
    _deleteById(persistentClass, id)
  }

  def save(entity: T): Boolean = {
    _saveOrUpdateIsNew(entity)
  }

  def search(search: ISearch): List[T] = {
    _search(persistentClass, search).asScala.toList.asInstanceOf[List[T]]
  }

  def searchAndCount(search: ISearch): SearchResult[T] = {
    if (search == null) {
      val result: SearchResult[T] = new SearchResult[T]
      result.setResult(findAll.asInstanceOf[java.util.List[T]])
      result.setTotalCount(result.getResult.size)
      return result
    }
    _searchAndCount(persistentClass, search).asInstanceOf[SearchResult[T]]
  }

  def searchUnique(search: ISearch): T = {
    _searchUnique(persistentClass, search).asInstanceOf[T]
  }

}
