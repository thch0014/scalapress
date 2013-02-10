package com.liferay.scalapress.plugin.search

import javax.persistence.{Entity, Table, GenerationType, GeneratedValue, Id, JoinColumn, ManyToOne, Column}
import reflect.BeanProperty
import com.liferay.scalapress.enums.Sort
import com.liferay.scalapress.domain.{ObjectType, Folder}

/** @author Stephen Samuel */
@Entity
@Table(name = "searches")
class SavedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var status: String = _

    // search inside this folder
    @ManyToOne
    @JoinColumn(name = "searchCategory", nullable = true)
    @BeanProperty var searchFolder: Folder = _

    @BeanProperty var imageOnly: Boolean = _

    @BeanProperty var labels: String = _

    @ManyToOne
    @JoinColumn(name = "itemType", nullable = true)
    @BeanProperty var objectType: ObjectType = _

    @JoinColumn(name = "inStockOnly", nullable = false)
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

    @BeanProperty var sortType: Sort = Sort.Name

    @Column(name = "limit")
    @BeanProperty var maxResults: Int = _

    @Column(name = "sellPriceTo")
    @BeanProperty var maxPrice: Int = _

    @Column(name = "sellPriceFrom")
    @BeanProperty var minPrice: Int = _

    // find objects newer than this date
    @Column(name = "createdAfter")
    @BeanProperty var newerThanTimestamp: Long = _
}