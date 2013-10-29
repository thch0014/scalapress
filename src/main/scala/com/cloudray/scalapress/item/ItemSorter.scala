package com.cloudray.scalapress.item

import com.cloudray.scalapress.item.attr.{Attribute, AttributeFuncs}
import scala.util.Random
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
object ItemSorter {

  def sort(objs: Iterable[Item],
           sort: Sort,
           sortAttribute: Option[Attribute],
           seed: Long = System.currentTimeMillis): Seq[Item] = {
    (sort, sortAttribute) match {

      case (Sort.Attribute, Some(attribute)) =>
        objs.toSeq.sortBy(obj => AttributeFuncs.attributeValue(obj, attribute).getOrElse(""))

      case (Sort.AttributeDesc, Some(attribute)) =>
        objs.toSeq.sortBy(obj => AttributeFuncs.attributeValue(obj, attribute).getOrElse("")).reverse

      case (Sort.Price, _) => objs.toSeq.sortBy(_.price)
      case (Sort.PriceHigh, _) => objs.toSeq.sortBy(_.price).reverse
      case (Sort.Newest, _) => objs.toSeq.sortBy(_.id).reverse
      case (Sort.Oldest, _) => objs.toSeq.sortBy(_.id)
      case (Sort.Random, _) => new Random(seed).shuffle(objs.toSeq)
      case _ => objs.toSeq.sortBy(_.name)
    }
  }
}
