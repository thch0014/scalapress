package com.liferay.scalapress.domain

import javax.persistence.{CollectionTable, Entity, Table, ElementCollection, GenerationType, GeneratedValue, Id, JoinColumn, ManyToOne, Column}
import reflect.BeanProperty
import java.util
import com.liferay.scalapress.enums.Sort

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

    @ElementCollection
    @CollectionTable(
        name = "searches_labels",
        joinColumns = Array(new JoinColumn(name = "search_id"))
    )
    @BeanProperty var labels: java.util.Set[String] = new util.HashSet[String]()

    @ManyToOne
    @JoinColumn(name = "itemType", nullable = true)
    @BeanProperty var objectType: ObjectType = _

    @JoinColumn(name = "inStockOnly", nullable = false)
    @BeanProperty var inStockOnly: Boolean = _

    // search object name and content
    @BeanProperty var keywords: String = _
    @BeanProperty var name: String = _

    @Column(name = "itemTypes")
    @ElementCollection
    @BeanProperty var objectTypes: java.util.Set[Integer] = new java.util.HashSet[Integer]()

    @Column(name = "multipleItemTypes", nullable = false)
    @BeanProperty var multipleObjectTypes: Boolean = _

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