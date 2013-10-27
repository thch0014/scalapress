package com.cloudray.scalapress.obj.attr

import scala.Predef.String
import javax.persistence.{JoinColumn, FetchType, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import scala.beans.BeanProperty
import com.cloudray.thirdparty.NaturalOrderComparator

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_options")
class AttributeOption extends Ordered[AttributeOption] {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute")
  @BeanProperty
  var attribute: Attribute = _

  @BeanProperty
  var value: String = _

  @BeanProperty
  var position: Int = _

  def compare(that: AttributeOption): Int =
    new NaturalOrderComparator().compare(Option(value).getOrElse(""), Option(that.value).getOrElse(""))
}

object AttributeOption {
  def apply(id: Long, attribute: Attribute, value: String, position: Int): AttributeOption = {
    val option = new AttributeOption
    option.id = id
    option.attribute = attribute
    option.value = value
    option.position = position
    option
  }
}