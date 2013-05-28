package com.cloudray.scalapress.section

import scala.collection.JavaConverters._

/** @author Stephen Samuel */
trait SortedSections {

    var sections: java.util.Set[Section]
    def sortedSections: Seq[Section] = sections.asScala.toSeq.sortBy(_.id).sortBy(_.position)
}
