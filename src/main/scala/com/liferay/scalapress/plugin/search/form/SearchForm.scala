package com.liferay.scalapress.plugin.search.form

import javax.persistence.{Column, OneToMany, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Markup

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms")
class SearchForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @BeanProperty var itemtype: Long = _

    @Column(name = "resultsperpage")
    @BeanProperty var pageSize: Int = _

    @ManyToOne
    @JoinColumn(name = "markup", nullable = true)
    @BeanProperty var markup: Markup = _

    @OneToMany(mappedBy = "searchForm")
    @BeanProperty var fields: java.util.List[SearchFormField] = new java.util.ArrayList[SearchFormField]()

}
