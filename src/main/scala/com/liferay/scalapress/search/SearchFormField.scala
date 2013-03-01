package com.liferay.scalapress.search

import javax.persistence.{EnumType, Enumerated, JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.domain.attr.Attribute
import org.hibernate.annotations.{FetchMode, Fetch}

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

    @Column(name = "hidden")
    @BeanProperty var preset: Boolean = _

    @Column(name = "value")
    @BeanProperty var value: String = _

    @Column(name = "position")
    @BeanProperty var position: Int = _

    @ManyToOne
    @JoinColumn(name = "searchForm")
    @Fetch(FetchMode.JOIN)
    @BeanProperty var searchForm: SearchForm = _

    @ManyToOne
    @JoinColumn(name = "attribute")
    @Fetch(FetchMode.JOIN)
    @BeanProperty var attribute: Attribute = _

}
