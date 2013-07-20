package com.cloudray.scalapress.plugin.variations

import scala.beans.BeanProperty
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import java.lang.String
import javax.persistence._
import java.util
import org.hibernate.annotations._
import javax.persistence.Table
import javax.persistence.CascadeType
import javax.persistence.Entity
import scala.collection.JavaConverters._

/** @author Stephen Samuel
  *
  *         Eg, color or size
  **/
@Entity
@Table(name = "plugin_variations_dimensions")
class Dimension {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "object_type")
  @BeanProperty var objectType: ObjectType = _

  @BeanProperty var name: String = _
}

// particular value of a dimension, eg Red or Green
@Entity
@Table(name = "plugin_variations_dimensions_values")
class DimensionValue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @Index(name = "dimension_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dimension")
  @BatchSize(size = 50)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var dimension: Dimension = _

  @Index(name = "variation_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variation", nullable = true)
  @BatchSize(size = 20)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var variation: Variation = _

  @BeanProperty var value: String = _

  def copy = {
    val dv = new DimensionValue
    dv.dimension = dimension
    dv.value = value
    dv
  }
}

// a collection of dimension values associated with a given object, with stock and price
@Entity
@Table(name = "plugin_variations_variation")
class Variation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @Index(name = "object_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "object", nullable = true)
  @BatchSize(size = 20)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var obj: Obj = _

  @OneToMany(mappedBy = "variation", fetch = FetchType.EAGER, cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @BatchSize(size = 200)
  @Fetch(value = FetchMode.JOIN)
  @BeanProperty var dimensionValues: java.util.Set[DimensionValue] = new util.HashSet[DimensionValue]()

  @BeanProperty var price: Int = _
  @BeanProperty def vat: Int = (price * obj.vatRate / 100.0).toInt
  @BeanProperty def priceInc: Int = price + vat
  @BeanProperty var stock: Int = _

  def name = dimensionValues.asScala.toSeq.sortBy(_.dimension.id).map(_.value).mkString(" ")
}