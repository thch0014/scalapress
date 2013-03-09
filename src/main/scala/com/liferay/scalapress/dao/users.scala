package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.User
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */

trait UserDao extends GenericDao[User, java.lang.Long] {
    def byUsername(s: String): User
}

@Component
@Transactional
class UserDaoImpl extends GenericDaoImpl[User, java.lang.Long] with UserDao {
    @Transactional
    def byUsername(uname: String): User = searchUnique(new Search(classOf[User]).addFilterEqual("username", uname))
}