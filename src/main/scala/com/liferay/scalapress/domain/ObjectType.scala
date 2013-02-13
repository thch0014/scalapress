package com.liferay.scalapress.domain

import attr.Attribute
import javax.persistence.{ElementCollection, JoinColumn, ManyToOne, CascadeType, OneToMany, FetchType, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import java.util
import reflect.BeanProperty
import com.liferay.scalapress.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "items_types")
class ObjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @Column
    @BeanProperty var name: String = _

    @BeanProperty var linkGroups: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
    @BeanProperty var attributes: java.util.List[Attribute] = new util.ArrayList[Attribute]()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType")
    @BeanProperty var sections: java.util.List[Section] = new util.ArrayList[Section]()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listitemmarkup", nullable = true)
    @BeanProperty var objectListMarkup: Markup = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objectViewMarkup", nullable = true)
    @BeanProperty var objectViewMarkup: Markup = _

    def bootIcon = name.toLowerCase match {
        case "event" | "events" => "icon-calendar"
        case "job" | "jobs" => "icon-truck"
        case "product" | "products" => "icon-th-large"
        case _ => "icon-circle-blank"
    }
}
