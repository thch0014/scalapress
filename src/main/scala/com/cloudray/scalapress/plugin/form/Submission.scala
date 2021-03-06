package com.cloudray.scalapress.plugin.form

import javax.persistence.{FetchType, Column, ElementCollection, CascadeType, OneToMany, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}
import com.cloudray.scalapress.util.{WebPage, Page}

/** @author Stephen Samuel */
@Entity
@Table(name = "forms_submissions")
class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @OneToMany(mappedBy = "submission", cascade = Array(CascadeType.ALL))
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var data: java.util.List[SubmissionKeyValue] = new java.util.ArrayList[SubmissionKeyValue]

  @Column(name = "name")
  @BeanProperty
  var formName: String = _

  @BeanProperty
  var date: Long = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var folder: Folder = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var obj: Item = _

  @BeanProperty
  var ipAddress: String = _

  @ElementCollection
  @BeanProperty
  var attachments: java.util.Set[String] = new java.util.HashSet[String]

  def page: Option[WebPage] = {
    if (obj != null) Some(WebPage(obj))
    else if (folder != null) Some(WebPage(folder))
    else None
  }
}
