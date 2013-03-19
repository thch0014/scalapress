package com.liferay.scalapress.domain.attr

import java.lang.String
import javax.persistence.{FetchType, JoinColumn, ManyToOne, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.search.SavedSearch
import org.hibernate.annotations.{Index, BatchSize}
import com.liferay.scalapress.plugin.listings.ListingProcess
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_values")
class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Index(name = "attribute_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute", nullable = true)
    @BatchSize(size = 50)
    @BeanProperty var attribute: Attribute = _

    @Index(name = "object_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = true)
    @BatchSize(size = 20)
    @BeanProperty var obj: Obj = _

    @Index(name = "search_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search", nullable = true)
    @BatchSize(size = 20)
    @BeanProperty var savedSearch: SavedSearch = _

    @Index(name = "lp_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_process", nullable = true)
    @BatchSize(size = 20)
    @BeanProperty var listingProcess: ListingProcess = _

    @BeanProperty var value: String = _
}
