package com.liferay.scalapress.settings

import javax.persistence._
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_site")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    // email for the software to send emails to
    @BeanProperty var adminEmail: String = _

    @BeanProperty var postcode: String = null

    // public contact email
    @BeanProperty var email: String = null

    @BeanProperty var telephone: String = null

    @BeanProperty var address: String = null

    @BeanProperty var name: String = null

    @BeanProperty var vatNumber: String = _
    def vatEnabled: Boolean = Option(vatNumber).filter(_.trim.length > 0).isDefined

    @BeanProperty var companyNumber: String = _

    @BeanProperty var country: String = null

    @BeanProperty var domain: String = "localhost"
}
