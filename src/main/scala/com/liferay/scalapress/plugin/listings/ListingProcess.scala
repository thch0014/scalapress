package com.liferay.scalapress.plugin.listings

import javax.persistence.{Table, Entity, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_listings_process")
class ListingProcess {

    @Id
    @BeanProperty var sessionId: String = _

}
