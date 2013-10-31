package com.cloudray.scalapress.plugin.form.section

import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence.{ManyToOne, Entity, Table, JoinColumn}
import com.cloudray.scalapress.plugin.form.{FormDao, Form}
import com.cloudray.scalapress.section.Section
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.form.controller.renderer.FormRenderer

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
    val rendered = FormRenderer.render(form, req)
    Option(rendered)
  }

  override def _init(context: ScalapressContext) {
    form = context.bean[FormDao].findAll.head
  }
}
