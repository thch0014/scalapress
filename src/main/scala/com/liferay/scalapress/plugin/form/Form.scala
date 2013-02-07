package com.liferay.scalapress.plugin.form

import javax.persistence.{ElementCollection, Column, OneToMany, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "forms")
class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @BeanProperty var captcha: Boolean = _

    @BeanProperty var submitButtonText: String = _

    @OneToMany(mappedBy = "form")
    @BeanProperty var fields: java.util.List[FormField] = new java.util.ArrayList[FormField]

    // shows on final page
    @BeanProperty var submissionMessage: String = _
    // redirect on completion
    @Column(name = "submissionforward")
    @BeanProperty var submissionRedirect: String = _
    // shows in email
    @Column(name = "submissionEmailMessage")
    @BeanProperty var submissionEmailBody: String = _
    @BeanProperty var submissionScript: String = _

    @Column(name = "emails")
    @ElementCollection
    @BeanProperty var recipients: java.util.List[String] = new java.util.ArrayList[String]

    @BeanProperty var cssId: String = _
    @BeanProperty var cssClass: String = _

    @Column(name = "ccItemListers")
    @BeanProperty var emailObjectOwner: Boolean = _

}