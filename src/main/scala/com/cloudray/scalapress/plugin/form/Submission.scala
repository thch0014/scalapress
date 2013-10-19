package com.cloudray.scalapress.plugin.form

import javax.persistence.{FetchType, Column, ElementCollection, CascadeType, OneToMany, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}
import com.cloudray.scalapress.util.Page

/** @author Stephen Samuel */
@Entity
@Table(name = "forms_submissions")
class Submission {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @OneToMany(mappedBy = "submission", cascade = Array(CascadeType.ALL))
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var data: java.util.List[SubmissionKeyValue] = new java.util.ArrayList[SubmissionKeyValue]

  @Column(name = "name")
  @BeanProperty var formName: String = _

  @BeanProperty var date: Long = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var folder: Folder = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var obj: Obj = _

  def page: Option[Page] = {
    if (obj != null)
      Some(Page(obj))
    else if (folder != null)
      Some(Page(folder))
    else
      None
  }

  @BeanProperty var ipAddress: String = _

  @ElementCollection
  @BeanProperty var attachments: java.util.Set[String] = new java.util.HashSet[String]
}
