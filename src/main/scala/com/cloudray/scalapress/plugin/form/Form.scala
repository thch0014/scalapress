package com.cloudray.scalapress.plugin.form

import scala.collection.JavaConverters._
import javax.persistence._
import scala.beans.BeanProperty

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

  // text used on the web page
  @Column(name = "submissionmessage", length = 10000)
  @BeanProperty var submissionText: String = _

  @OneToMany(mappedBy = "form", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @BeanProperty var fields: java.util.List[FormField] = new java.util.ArrayList[FormField]

  // redirect on completion
  @Column(name = "submissionforward", length = 1000)
  @BeanProperty var submissionRedirect: String = _

  // shows in email
  @Column(name = "submissionEmailMessage", length = 10000)
  @BeanProperty var submissionEmailBody: String = _

  @Column(name = "submissionEmailSubject", length = 1000)
  @BeanProperty var submissionEmailSubject: String = _

  @Column(name = "submissionScript", length = 10000)
  @BeanProperty var submissionScript: String = _

  @Column(name = "emails", length = 1000)
  @BeanProperty var recipients: String = _

  @BeanProperty var cssId: String = _
  @BeanProperty var cssClass: String = _

  @Column(name = "ccItemListers")
  @BeanProperty var emailObjectOwner: Boolean = _

  def submissionField: Option[FormField] = fields.asScala.find(_.submitterEmailField)
}
