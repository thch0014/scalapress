package com.liferay.scalapress.search

import javax.persistence.{EnumType, Enumerated, JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.domain.attr.Attribute

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms_fields")
class SearchFormField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    @BeanProperty var fieldType: SearchFieldType = _

    @Column(name = "name")
    @BeanProperty var name: String = _

    @ManyToOne
    @JoinColumn(name = "searchForm")
    @BeanProperty var searchForm: SearchForm = _

    @ManyToOne
    @JoinColumn(name = "attribute")
    @BeanProperty var attribute: Attribute = _

}
