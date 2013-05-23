package com.liferay.scalapress.plugin.variations

import javax.persistence._
import scala.beans.BeanProperty
import org.hibernate.annotations._
import com.liferay.scalapress.obj.{ObjectType, Obj}
import java.lang.String
import javax.persistence.CascadeType
import java.util

/** @author Stephen Samuel
  *
  *         Eg, color or size
  **/
class Dimension {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _
    @BeanProperty var objectType: ObjectType = _
    @BeanProperty var name: String = _
}

// particular value of a dimension, eg Red or Green
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
    @BeanProperty var obj: Variation = _

    @BeanProperty var value: String = _

}

// a collection of dimension values associated with a given object, with stock and price
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

    @OneToMany(mappedBy = "variation", fetch = FetchType.LAZY,
        cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var attributeValues: java.util.Set[DimensionValue] = new util.HashSet[DimensionValue]()

    @BeanProperty var price: Int = _
    @BeanProperty var stock: Int = _

}