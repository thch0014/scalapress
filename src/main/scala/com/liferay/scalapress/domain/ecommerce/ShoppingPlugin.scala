package com.liferay.scalapress.domain.ecommerce

import scala.Predef.String
import reflect.BeanProperty
import javax.persistence.{GenerationType, GeneratedValue, Id, Entity, Table}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_shopping")
class ShoppingPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var checkoutScripts: String = _
    @BeanProperty var basketMarkup: String = _
    @BeanProperty var checkoutConfirmationMarkup: String = _
}
