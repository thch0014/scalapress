package com.liferay.scalapress.obj

import attr.Attribute
import javax.persistence._
import java.util
import org.hibernate.annotations.{CacheConcurrencyStrategy, BatchSize, FetchMode, Fetch}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.Markup
import scala.collection.JavaConverters._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "items_types")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class ObjectType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @BeanProperty var deleted: Boolean = false

    def searchable: Boolean = !name.toLowerCase.contains("account")

    @BeanProperty var prices: Boolean = false

    @BeanProperty var linkGroups: String = _
    def linkGroupsArray = Option(linkGroups)
      .map(_.split("\n").map(_.trim).filter(_.length > 0))
      .getOrElse(Array[String]())

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.JOIN)
    @BeanProperty var attributes: java.util.Set[Attribute] = new util.HashSet[Attribute]()

    def sortedAttributes = attributes.asScala.toSeq.sortBy(_.position)

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
        case "listing" | "listings" => "icon-list-alt"
        case "event" | "events" | "show" | "shows" => "icon-calendar"
        case "job" | "jobs" => "icon-truck"
        case "image" | "images" => "icon-picture"
        case "product" | "products" => "icon-th-large"
        case "membership" | "account" | "accounts" => "icon-user"
        case _ => "icon-circle-blank"
    }
}
