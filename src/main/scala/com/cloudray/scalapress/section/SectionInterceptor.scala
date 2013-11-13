package com.cloudray.scalapress.section

import com.cloudray.scalapress.section.Section

/** @author Stephen Samuel */
trait SectionInterceptor {
  def preSection(section: Section): Unit = {}
  def postSection(section: Section): Unit = {}
}
