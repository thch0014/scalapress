package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
trait OrderDao {
    def get(id: Long): Order
}

@Component
@Transactional
class OrderDaoImpl extends HibernateBaseDAO with OrderDao {

    @Autowired
    override def setSessionFactory(sessionFactory: SessionFactory) {
        super.setSessionFactory(sessionFactory)
    }

    @Transactional
    override def get(id: Long) = getSession.get(classOf[Order], id).asInstanceOf[Order]

}
