package com.cloudray.scalapress.item

import attr.Attribute
import javax.persistence._
import java.util
import org.hibernate.annotations._
import com.cloudray.scalapress.section.{SortedSections, Section}
import com.cloudray.scalapress.theme.Markup
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import javax.persistence.Table
import javax.persistence.CascadeType
import javax.persistence.Entity

/** @author Stephen Samuel */
@Entity
@Table(name = "items_types")
class ItemType extends SortedSections with java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: java.lang.Long = _

  @BeanProperty
  var name: String = _

  @Column(name = "deleted", nullable = false)
  @BeanProperty
  var deleted: Boolean = false

  @Column(name = "hidden", nullable = false)
  @BeanProperty
  var hidden: Boolean = false

  def searchable: Boolean = !hidden

  @Column(name = "prices", nullable = false)
  @BeanProperty
  var prices: Boolean = false

  @BeanProperty var linkGroups: String = _
  def linkGroupsArray = Option(linkGroups)
    .map(_.split("\n").map(_.trim).filter(_.length > 0))
    .getOrElse(Array[String]())

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var attributes: java.util.Set[Attribute] = new util.HashSet[Attribute]()
  def sortedAttributes: Seq[Attribute] = attributes.asScala.toSeq.sortBy(_.id).sortBy(_.position)

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "objectType", cascade = Array(CascadeType.ALL))
  @Fetch(FetchMode.SELECT)
  @BatchSize(size = 10)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var sections: java.util.Set[Section] = new util.HashSet[Section]()

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "listitemmarkup", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var objectListMarkup: Markup = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "objectViewMarkup", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var objectViewMarkup: Markup = _

  def bootIcon = name.toLowerCase match {
    case "listing" | "listings" => "glyphicon glyphicon-list-alt"
    case "event" | "events" | "show" | "shows" => "glyphicon glyphicon-calendar"
    case "job" | "jobs" => "glyphicon glyphicon-truck"
    case "image" | "images" => "glyphicon glyphicon-picture"
    case "product" | "products" => "glyphicon glyphicon-th-large"
    case "membership" | "account" | "accounts" => "glyphicon glyphicon-user"
    case "news" => "icon-file-alt"
    case _ => "glyphicon glyphicon-record"
  }
}
