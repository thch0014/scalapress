package com.liferay.scalapress.plugin.listings

import javax.persistence.{Column, OneToMany, CascadeType, FetchType, ManyToOne, Table, Entity, Id}
import reflect.BeanProperty
import scala.Array
import org.hibernate.annotations.{BatchSize, FetchMode, Fetch}
import com.liferay.scalapress.domain.attr.AttributeValue
import java.util

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_listings_inprocess")
class ListingProcess {

    @Id
    @BeanProperty var sessionId: String = _

    @Column(length = 10000)
    @BeanProperty var content: String = _

    @BeanProperty var email: String = _

    // the last account that was logged in on this listing which might change
    @BeanProperty var accountId: String = _

    @ManyToOne
    @BeanProperty var listingPackage: ListingPackage = _

    @BeanProperty var folders: Array[Long] = Array()

    @BeanProperty var title: String = _

    @BeanProperty var imageKeys: Array[String] = Array()

    @OneToMany(mappedBy = "listingProcess", fetch = FetchType.LAZY,
        cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var attributeValues: java.util.Set[AttributeValue] = new util.HashSet[AttributeValue]()
}
