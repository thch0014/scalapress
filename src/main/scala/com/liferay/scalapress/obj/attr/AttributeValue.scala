package com.liferay.scalapress.obj.attr

import java.lang.String
import javax.persistence._
import reflect.BeanProperty
import com.liferay.scalapress.search.SavedSearch
import org.hibernate.annotations._
import com.liferay.scalapress.plugin.listings.ListingProcess
import com.liferay.scalapress.obj.Obj
import javax.persistence.Table
import javax.persistence.Entity

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_values")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Index(name = "attribute_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute", nullable = true)
    @BatchSize(size = 50)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var attribute: Attribute = _

    @Index(name = "object_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = true)
    @BatchSize(size = 20)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var obj: Obj = _

    @Index(name = "search_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search", nullable = true)
    @BatchSize(size = 20)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var savedSearch: SavedSearch = _

    @Index(name = "lp_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_process", nullable = true)
    @BatchSize(size = 20)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var listingProcess: ListingProcess = _

    @BeanProperty var value: String = _
}
