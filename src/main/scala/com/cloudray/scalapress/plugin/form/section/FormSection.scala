package com.cloudray.scalapress.plugin.form.section

import javax.persistence.{ManyToOne, Entity, Table, JoinColumn}
import com.cloudray.scalapress.plugin.form.Form
import com.cloudray.scalapress.section.Section
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.form.controller.renderer.FormRenderer
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_forms")
class FormSection extends Section {

  @ManyToOne
  @JoinColumn(name = "form")
  @BeanProperty
  var form: Form = _

  def desc: String = "For showing a form on a folder or object page"
  override def backoffice: String = "/backoffice/plugin/form/section/" + id
  def render(req: ScalapressRequest): Option[String] = {
    Option(form).map(FormRenderer.render(_, req))
  }
}
