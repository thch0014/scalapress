package com.liferay.scalapress.theme

import javax.persistence._
import reflect.BeanProperty
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Entity
@Table(name = "templates")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @Column(name = "header", length = 10000, nullable = true)
    @BeanProperty var header: String = _

    @Column(name = "footer", length = 10000, nullable = true)
    @BeanProperty var footer: String = _

    @Column(name = "dfault")
    @BeanProperty var default: Boolean = _

}
