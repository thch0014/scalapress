package com.liferay.scalapress.plugin.form.section

import com.liferay.scalapress.{Section, ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import javax.persistence.{ManyToOne, Entity, Table, JoinColumn}
import com.liferay.scalapress.service.render.FormRenderer
import com.liferay.scalapress.plugin.form.Form

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_forms")
class FormSection extends Section {

    @ManyToOne
    @JoinColumn(name = "form")
    @BeanProperty var form: Form = _

    def desc: String = "For showing a form on a folder or object page"
    def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {
        val rendered = FormRenderer.render(form, req)
        Option(rendered)
    }
}