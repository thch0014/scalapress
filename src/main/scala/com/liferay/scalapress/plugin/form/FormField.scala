package com.liferay.scalapress.plugin.form

import javax.persistence.{EnumType, Enumerated, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import scala.Predef.String
import com.liferay.scalapress.enums.{FieldSize, FormFieldType}
import scala.beans.BeanProperty
import javax.persistence.Column

/** @author Stephen Samuel */
@Entity
@Table(name = "forms_fields")
class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "form")
    @BeanProperty var form: Form = _

    @Column(name = "mandatory")
    @BeanProperty var required: Boolean = _

    @Column(name = "size")
    @BeanProperty var size: FieldSize = FieldSize.Medium

    @Column(name = "regExp")
    @BeanProperty var regExp: String = _

    @BeanProperty var name: String = _

    @BeanProperty var placeholder: String = _

    @Column(name = "submitterEmail", nullable = false)
    @BeanProperty var submitterEmailField: Boolean = true

    @Column(name = "options")
    @BeanProperty var options: String = _
    def optionsList: Array[String] = Option(options).getOrElse("").split("\n")

    @BeanProperty var position: Int = _

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @BeanProperty var fieldType: FormFieldType = FormFieldType.Text
}
