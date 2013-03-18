package com.liferay.scalapress.plugin.form.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import javax.persistence.{ManyToOne, Entity, Table, JoinColumn}
import com.liferay.scalapress.plugin.form.{FormRenderer, Form}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_forms")
class FormSection extends Section {

    @ManyToOne
    @JoinColumn(name = "form")
    @BeanProperty var form: Form = _

    def desc: String = "For showing a form on a folder or object page"
    override def backoffice: String = "/backoffice/plugin/form/section/" + id
    def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {
        val rendered = FormRenderer.render(form, req)
        Option(rendered)
    }

    override def _init(context: ScalapressContext) {
        form = context.formDao.findAll().head
    }
}
