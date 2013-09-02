package com.cloudray.scalapress.obj

import javax.persistence.{Table, Entity, Column}
import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_objects_content")
class ObjectContentSection extends Section {

  @Column(name = "content")
  var content: String = _

  def desc = "Edit and then display a section of content when viewing this object"

  def render(request: ScalapressRequest): Option[String] = Option(content)
}
