package com.cloudray.scalapress.section

/** @author Stephen Samuel */
trait SectionInterceptor {
  def preSection(section: Section): Unit = {}
  def postSection(section: Section): Unit = {}
}
