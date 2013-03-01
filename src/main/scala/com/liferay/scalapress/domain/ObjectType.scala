package com.liferay.scalapress.domain

import attr.Attribute
import javax.persistence.{JoinColumn, ManyToOne, CascadeType, OneToMany, FetchType, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import java.util
import reflect.BeanProperty
import org.hibernate.annotations.{BatchSize, FetchMode, Fetch}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "items_types")
class ObjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @BeanProperty var deleted: Boolean = false

    @BeanProperty var linkGroups: String = _
    def linkGroupsArray = Option(linkGroups)
      .map(_.split("\n").map(_.trim).filter(_.length > 0))
      .getOrElse(Array[String]())

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.JOIN)
    @BeanProperty var attributes: java.util.Set[Attribute] = new util.HashSet[Attribute]()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 10)
    @BeanProperty var sections: java.util.Set[Section] = new util.HashSet[Section]()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listitemmarkup", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BeanProperty var objectListMarkup: Markup = _

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "objectViewMarkup", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BeanProperty var objectViewMarkup: Markup = _

    def bootIcon = name.toLowerCase match {
        case "event" | "events" => "icon-calendar"
        case "job" | "jobs" => "icon-truck"
        case "product" | "products" => "icon-th-large"
        case _ => "icon-circle-blank"
    }
}
