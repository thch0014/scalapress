package com.cloudray.scalapress.item.attr

import javax.persistence._
import com.cloudray.scalapress.search.SavedSearch
import org.hibernate.annotations._
import com.cloudray.scalapress.item.Item
import javax.persistence.Table
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.listings.domain.ListingProcess
import scala.Predef._

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_values")
class AttributeValue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @Index(name = "attribute_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var attribute: Attribute = _

  @Index(name = "object_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var item: Item = _

  @Index(name = "search_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "search", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var savedSearch: SavedSearch = _

  @Index(name = "lp_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "listing_process", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var listingProcess: ListingProcess = _

  @BeanProperty
  var value: String = _

  override def toString: String = s"AttributeValue [item=$item attribute=$attribute value=$value]"

  override def hashCode: Int =
    Option(value).map(_.hashCode).getOrElse(1) * Option(attribute).map(_.id.hashCode).getOrElse(1)
  override def equals(obj: scala.Any): Boolean = obj match {
    case other: AttributeValue => other.attribute == attribute && other.value == value
    case _ => false
  }
}

case class AttributeSelection(id: String, value: String)
object AttributeSelection {
  def apply(id: Long, value: String): AttributeSelection = apply(id.toString, value)
}