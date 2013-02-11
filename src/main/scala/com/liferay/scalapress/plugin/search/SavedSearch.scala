package com.liferay.scalapress.plugin.search

import javax.persistence.{EnumType, Enumerated, Entity, Table, GenerationType, GeneratedValue, Id, JoinColumn, Column}
import reflect.BeanProperty
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
    @Column(name = "searchCategory", nullable = true)
    @BeanProperty var searchFolders: String = _

    @BeanProperty var imageOnly: Boolean = _

    @Column(name = "method", nullable = true)
    @BeanProperty var labels: String = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var sortType: Sort = _

    @Column(name = "itemType", nullable = true)
    @BeanProperty var objectType: Long = _

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

    @Column(name = "limit")
    @BeanProperty var maxResults: Int = _

    @Column(name = "sellPriceTo")
    @BeanProperty var maxPrice: Int = _

    @Column(name = "sellPriceFrom")
    @BeanProperty var minPrice: Int = _

    // find objects newer than this date
    @BeanProperty var newerThanTimestamp: Long = _
}