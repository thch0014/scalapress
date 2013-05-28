package com.cloudray.scalapress.plugin.profile

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_account")
class AccountPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ElementCollection
    @BeanProperty var accounts: java.util.List[java.lang.Long] = new java.util.ArrayList[java.lang.Long]()

    @BeanProperty var accountPageHeader: String = _
    @BeanProperty var accountPageFooter: String = _
}