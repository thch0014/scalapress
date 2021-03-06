package com.cloudray.scalapress.section

import javax.persistence._
import org.hibernate.annotations.{NotFound, NotFoundAction, Index}
import com.cloudray.scalapress.item.{ItemType, Item}
import com.cloudray.scalapress.folder.Folder
import scala.beans.BeanProperty
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Section {

  def desc: String
  def render(request: ScalapressRequest): Option[String]
  def backoffice: String = "/backoffice/section/" + id

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @BeanProperty
  var id: Long = _

  @Column(name = "visible", nullable = false)
  @BeanProperty
  var visible: Boolean = _

  @Column(name = "name")
  @BeanProperty
  var name: String = _

  @Column(name = "position")
  @BeanProperty
  var position: Int = _

  @ManyToOne
  @JoinColumn(name = "ownerCategory", nullable = true)
  @Index(name = "folder_index")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var folder: Folder = _

  @ManyToOne
  @JoinColumn(name = "ownerItem", nullable = true)
  @Index(name = "object_index")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var item: Item = _

  @ManyToOne
  @JoinColumn(name = "ownerItemType", nullable = true)
  @Index(name = "objecttype_index")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var objectType: ItemType = _

  final def init(context: ScalapressContext) {
    _init(context)
    visible = true
    context.sectionDao.save(this)
  }
  def _init(context: ScalapressContext) {}
  override def toString: String = getClass.getSimpleName + s" [id=$id]"
}
