package com.cloudray.scalapress.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

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

@Component
class UserDaoInit {

    @Autowired var userDao: UserDao = _

    @PostConstruct def ensureAUser() {
        if (userDao.findAll().size == 0) {
            val user = defaultUser
            userDao.save(user)
        }
    }

    def defaultUser = {
        val user = new User
        user.username = "admin"
        user.name = "admin"
        user.passwordHash = "09b792e75d96dbcb3d49f5af313e9fa1"
        user.active = true
        user
    }
}