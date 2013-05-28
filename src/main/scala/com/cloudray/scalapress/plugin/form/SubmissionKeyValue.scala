package com.cloudray.scalapress.plugin.form

import javax.persistence.{Entity, GenerationType, GeneratedValue, Id, JoinColumn, Column, Table, ManyToOne}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}

/** @author Stephen Samuel */
@Entity
@Table(name = "forms_submissions_data")
class SubmissionKeyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "submission")
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var submission: Submission = _

    @Column(name = "name")
    @BeanProperty var key: String = _
    @BeanProperty var value: String = _

}
