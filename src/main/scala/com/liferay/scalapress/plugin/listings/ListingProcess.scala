package com.liferay.scalapress.plugin.listings

import javax.persistence.{ManyToOne, Table, Entity, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_listings_inprocess")
class ListingProcess {

    @Id
    @BeanProperty var sessionId: String = _

    @ManyToOne
    @BeanProperty var listingPackage: ListingPackage = _
}
