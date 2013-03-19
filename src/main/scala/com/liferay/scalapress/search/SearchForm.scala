package com.liferay.scalapress.search

import javax.persistence.{EnumType, Enumerated, JoinColumn, ManyToOne, CascadeType, Column, OneToMany, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import org.hibernate.annotations.{Fetch, FetchMode}
import com.liferay.scalapress.enums.Sort
import com.liferay.scalapress.obj.ObjectType

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms")
class SearchForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @ManyToOne
    @JoinColumn(name = "itemtype")
    @BeanProperty var objectType: ObjectType = _

    @Column(name = "resultsperpage")
    @BeanProperty var pageSize: Int = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var sort: Sort = _

    @BeanProperty var submitLabel: String = _

    @OneToMany(mappedBy = "searchForm", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.JOIN)
    @BeanProperty var fields: java.util.Set[SearchFormField] = new java.util.HashSet[SearchFormField]()

}
