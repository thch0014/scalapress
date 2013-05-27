package com.liferay.scalapress.plugin.profile

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "password_token")
class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @Column(name = "email")
    @BeanProperty var email: String = _

    @Column(name = "token")
    @BeanProperty var token: String = _
}
