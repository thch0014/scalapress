package com.liferay.scalapress.plugin.form

import reflect.BeanProperty
import javax.persistence.{Entity, GenerationType, GeneratedValue, Id, JoinColumn, Column, Table, ManyToOne}

/** @author Stephen Samuel */
@Entity
@Table(name = "forms_submissions_data")
class SubmissionKeyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "submission")
    @BeanProperty var submission: Submission = _

    @Column(name = "name")
    @BeanProperty var key: String = _
    @BeanProperty var value: String = _

}
