package com.cloudray.scalapress.folder

import com.cloudray.scalapress.section.Section

/** @author Stephen Samuel */
trait SectionInterceptor {
  def preSection(section: Section): Unit = {}
  def postSection(section: Section): Unit = {}
}
