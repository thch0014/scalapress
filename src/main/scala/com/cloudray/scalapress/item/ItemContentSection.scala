package com.cloudray.scalapress.item

import javax.persistence.{Table, Entity, Column}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_objects_content")
class ItemContentSection extends Section {

  @Column(name = "content")
  var content: String = _

  def desc = "Edit and then display a section of content when viewing this item"

  def render(request: ScalapressRequest): Option[String] = Option(content)
}
