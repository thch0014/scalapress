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
    def findDefault: Theme = findAll.find(_.default) match {
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
