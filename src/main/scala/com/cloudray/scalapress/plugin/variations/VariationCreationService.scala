package com.cloudray.scalapress.plugin.variations

import scala.collection.JavaConverters._
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class VariationCreationService(variationDao: VariationDao) {

  def create(obj: Item, map: Map[Dimension, Seq[String]]): Iterable[Variation] = {
    val values = mapToList(map)
    val combinations = combine(List(List()), values.map(_.toList).toList)

    val variations = for ( combination <- combinations ) yield {
      val v = new Variation
      v.obj = obj
      v.dimensionValues = combination.map(c => {
        val copy = c.copy
        copy.variation = v
        copy
      }).toSet.asJava
      v
    }

    val existing = variationDao.findByObjectId(obj.id)
    variations.filterNot(v => exists(v, existing))
  }

  def exists(variation: Variation, existing: Iterable[Variation]) = existing.exists(arg => equals(variation, arg))
  def equals(variation: Variation, other: Variation): Boolean =
    variation.dimensionValues.asScala.forall(dv => exists(dv, other.dimensionValues.asScala))
  def exists(dv: DimensionValue, existing: Iterable[DimensionValue]) =
    existing.exists(arg => arg.dimension.id == dv.dimension.id && arg.value == dv.value)

  def mapToList(map: Map[Dimension, Seq[String]]) = {
    for ( key <- map.keys ) yield {
      for ( value <- map(key) ) yield {
        val dv = new DimensionValue
        dv.dimension = key
        dv.value = value
        dv
      }
    }
  }

  def combine(combinations: List[List[DimensionValue]],
              remaining: List[List[DimensionValue]]): List[List[DimensionValue]] = {
    remaining.size match {
      case 0 => List(List())
      case _ =>
        for {
          x <- remaining.head
          xs <- combine(combinations, remaining.tail)
        } yield x :: xs
    }
  }
}
