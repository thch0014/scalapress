package com.liferay.scalapress.search

import javax.persistence.{FetchType, Column, OneToMany, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import org.hibernate.annotations.{Fetch, FetchMode}

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms")
class SearchForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @Column(name = "itemtype")
    @BeanProperty var objectType: Long = _

    @Column(name = "resultsperpage")
    @BeanProperty var pageSize: Int = _

    @BeanProperty var submitLabel: String = _

    @OneToMany(mappedBy = "searchForm")
    @Fetch(FetchMode.JOIN)
    @BeanProperty var fields: java.util.Set[SearchFormField] = new java.util.HashSet[SearchFormField]()

}
