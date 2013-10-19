package com.cloudray.scalapress.plugin.account

import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.theme.Markup

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_account")
class AccountPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @ManyToOne
  @JoinColumn(name = "accountPageMarkup")
  @BeanProperty var accountPageMarkup: Markup = _

  @ElementCollection
  @BeanProperty var accounts: java.util.List[java.lang.Long] = new java.util.ArrayList[java.lang.Long]()

  @Column(length = 10000)
  @BeanProperty var accountPageHeader: String = _

  @Column(length = 10000)
  @BeanProperty var accountPageFooter: String = _

  @Column(length = 10000)
  @BeanProperty var loginPageHeader: String = _

  @Column(length = 10000)
  @BeanProperty var loginPageFooter: String = _

  @Column(length = 10000)
  @BeanProperty var registrationPageHeader: String = _

  @Column(length = 10000)
  @BeanProperty var registrationPageFooter: String = _

  @BeanProperty var loginRedirect: String = _

}