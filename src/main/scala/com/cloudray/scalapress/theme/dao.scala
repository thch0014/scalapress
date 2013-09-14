package com.cloudray.scalapress.theme

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.hibernate.criterion.Restrictions
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait ThemeDao extends GenericDao[Theme, java.lang.Long] {
  def findDefault: Theme
}

@Component
@Transactional
class ThemeDaoImpl extends GenericDaoImpl[Theme, java.lang.Long] with ThemeDao {

  val EXPIRY_MS = 1000 * 60
  var _defaultExpiry: Long = 0
  var _default: Theme = _

  def findDefault: Theme = {
    if (_defaultExpiry > System.currentTimeMillis()) _default
    else _resetDefault
  }

  def _resetDefault: Theme = {
    _default = _loadDefault
    _defaultExpiry = System.currentTimeMillis() + EXPIRY_MS
    _default
  }

  def _loadDefault = findAll.find(_.default) match {
    case None => findAll.head
    case Some(t) => t
  }
}

trait MarkupDao extends GenericDao[Markup, java.lang.Long] {
  def byName(name: String): Markup
}

@Component
@Transactional
class MarkupDaoImpl extends GenericDaoImpl[Markup, java.lang.Long] with MarkupDao {

  @Transactional
  def byName(name: String): Markup = {
    getSession
      .createCriteria(classOf[Markup])
      .setCacheable(true)
      .add(Restrictions.eq("name", name))
      .uniqueResult()
      .asInstanceOf[Markup]
  }
}
