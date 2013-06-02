package com.cloudray.scalapress.obj

import com.cloudray.scalapress.enums.Sort
import com.cloudray.scalapress.obj.attr.{Attribute, AttributeFuncs}
import scala.util.Random

/** @author Stephen Samuel */
object ObjectSorter {

    def sort(objs: Iterable[Obj], sort: Sort, sortAttribute: Option[Attribute], seed: Long = System.currentTimeMillis): Seq[Obj] = {
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
