package com.liferay.scalapress.plugin.form

import scala.collection.JavaConverters._
import javax.persistence._
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "forms")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @BeanProperty var captcha: Boolean = _

    @BeanProperty var submitButtonText: String = _

    // text used on the web page
    @Column(name = "submissionmessage")
    @BeanProperty var submissionText: String = _

    @OneToMany(mappedBy = "form", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var fields: java.util.List[FormField] = new java.util.ArrayList[FormField]

    // redirect on completion
    @Column(name = "submissionforward")
    @BeanProperty var submissionRedirect: String = _

    // shows in email
    @Column(name = "submissionEmailMessage")
    @BeanProperty var submissionEmailBody: String = _
    @BeanProperty var submissionEmailSubject: String = _

    @BeanProperty var submissionScript: String = _

    @Column(name = "emails")
    @BeanProperty var recipients: String = _

    @BeanProperty var cssId: String = _
    @BeanProperty var cssClass: String = _

    @Column(name = "ccItemListers")
    @BeanProperty var emailObjectOwner: Boolean = _

    def submissionField: Option[FormField] = fields.asScala.find(_.submitterEmailField)
}
