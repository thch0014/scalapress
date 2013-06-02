package com.cloudray.scalapress.user

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class UserListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new UserListController
    controller.userDao = mock[UserDao]

    test("a created user is persisted") {
        controller.create
        Mockito.verify(controller.userDao).save(Matchers.any[User])
    }
}
