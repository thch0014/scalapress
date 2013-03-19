package com.liferay.scalapress.search

import javax.persistence.{ManyToOne, CascadeType, FetchType, OneToMany, EnumType, Enumerated, Entity, Table, GenerationType, GeneratedValue, Id, JoinColumn, Column}
import reflect.BeanProperty
import com.liferay.scalapress.enums.Sort
import scala.Array
import java.util
import org.hibernate.annotations.{FetchMode, Fetch}
import com.liferay.scalapress.obj.ObjectType
import com.liferay.scalapress.obj.attr.AttributeValue

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

    @OneToMany(mappedBy = "savedSearch", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @BeanProperty var attributeValues: java.util.Set[AttributeValue] = new util.HashSet[AttributeValue]()

    @BeanProperty var imageOnly: Boolean = _

    @Column(name = "method", nullable = true)
    @BeanProperty var labels: String = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var sortType: Sort = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemType")
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
}