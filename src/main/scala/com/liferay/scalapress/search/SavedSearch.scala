package com.liferay.scalapress.search

import javax.persistence._
import com.liferay.scalapress.enums.Sort
import scala.Array
import org.hibernate.annotations._
import com.liferay.scalapress.obj.ObjectType
import com.liferay.scalapress.obj.attr.{Attribute, AttributeValue}
import javax.persistence.Table
import javax.persistence.CascadeType
import javax.persistence.Entity
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "searches")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class SavedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var status: String = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sortAttribute")
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var sortAttribute: Attribute = _

    @BeanProperty var hasAttributes: String = _
    @BeanProperty var prefix: String = _

    // search inside this folder
    @Column(name = "searchCategory"
        , nullable = true)
    @BeanProperty var searchFolders: String = _

    @OneToMany(mappedBy = "savedSearch", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var attributeValues: java.util.Set[AttributeValue] = new java.util.HashSet[AttributeValue]()

    @BeanProperty var imageOnly: Boolean = _

    @Column(name = "method", nullable = true)
    @BeanProperty var labels: String = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var sortType: Sort = _

    @Column(name = "location", nullable = true)
    @BeanProperty var location: String = _

    @Column(name = "distance", nullable = true)
    @BeanProperty var distance: Int = 100

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemType")
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var objectType: ObjectType = _

    @Column(name = "inStockOnly", nullable = false)
    @BeanProperty var inStockOnly: Boolean = _

    // search all content
    @BeanProperty var keywords: String = _
    // search name only
    @BeanProperty var name: String = _

    //  @Column(name = "itemTypes")
    //   @ElementCollection
    //   @BeanProperty var objectTypes: java.util.Set[Integer] = new java.util.HashSet[Integer]()

    //    @Column(name = "multipleItemTypes", nullable = false)
    //  @BeanProperty var multipleObjectTypes: Boolean = _

    @Column(name = "limit")
    @BeanProperty var maxResults: Int = _

    @Column(name = "sellPriceTo")
    @BeanProperty var maxPrice: Int = _

    @Column(name = "sellPriceFrom")
    @BeanProperty var minPrice: Int = _

    // find objects newer than this date
    @BeanProperty var newerThanTimestamp: Long = _

    @Transient var facets: Seq[String] = Nil
}