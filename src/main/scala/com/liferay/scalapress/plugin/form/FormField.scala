package com.liferay.scalapress.plugin.form

import javax.persistence.{JoinColumn, ManyToOne, Column, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import scala.Predef.String
import com.liferay.scalapress.enums.FormFieldType

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

    @Column(name = "regExp")
    @BeanProperty var regExp: String = _

    @BeanProperty var name: String = _

    @BeanProperty var placeholder: String = _

    @Column(name = "options")
    @BeanProperty var options: String = _
    def optionsList: Array[String] = Option(options).getOrElse("").split("\n")

    @BeanProperty var position: Int = _

    @Column(name = "type")
    @BeanProperty var fieldType: FormFieldType = FormFieldType.Text
}
