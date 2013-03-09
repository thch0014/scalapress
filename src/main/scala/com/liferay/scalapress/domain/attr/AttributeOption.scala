package com.liferay.scalapress.domain.attr

import scala.Predef.String
import javax.persistence.{JoinColumn, FetchType, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_options")
class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute")
    @BeanProperty var attribute: Attribute = _

    @BeanProperty var value: String = _

    @BeanProperty var position: Int = _

    //    @BeanProperty var defaultForSearch: Boolean = false
    //  @BeanProperty var defaultForNew: Boolean = false

    //  @BeanProperty var location: String = _
    //    @BeanProperty var showValue: Boolean = _
    //  @BeanProperty var rnid: String = _
}
