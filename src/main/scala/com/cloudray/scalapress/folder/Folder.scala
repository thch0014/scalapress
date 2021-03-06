package com.cloudray.scalapress.folder

import javax.persistence._
import java.util
import org.hibernate.annotations._
import com.cloudray.scalapress.section.{SortedSections, Section}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.section.{ItemListSection, FolderContentSection, SubfolderSection}
import com.cloudray.scalapress.theme.Theme
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.CascadeType
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import org.joda.time.{DateTimeZone, DateTime}
import scala.Some

/** @author Stephen Samuel */
@Entity
@Table(name = "categories")
class Folder extends SortedSections with HtmlMeta {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "folders", cascade = Array(CascadeType.ALL))
  @Fetch(FetchMode.SELECT)
  @BeanProperty
  var items: java.util.Set[Item] = new java.util.HashSet[Item]()

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  @Fetch(FetchMode.SUBSELECT)
  @BatchSize(size = 40)
  @BeanProperty
  var subfolders: java.util.Set[Folder] = new java.util.HashSet[Folder]()

  // has to be array to work with velocity
  def sortedSubfolders: Array[Folder] = {
    val ordered = subfolders
      .asScala
      .filterNot(_.name == null)
      .toArray
      .sortBy(_.name)
    folderOrdering match {
      case FolderOrdering.Manual => ordered.sortBy(_.position)
      case _ => ordered
    }
  }

  @Index(name = "parent_index")
  @ManyToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "parent")
  @Fetch(FetchMode.JOIN)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var parent: Folder = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "theme")
  @BeanProperty
  var theme: Theme = _

  @Index(name = "name_index")
  @BeanProperty
  var name: String = _

  @Column(name = "hideTitle", nullable = false)
  @BeanProperty
  var hideTitle: Boolean = false

  @Column(name = "hideWidgets", nullable = false)
  @BeanProperty
  var hideWidgets: Boolean = false

  @Column(name = "subcategoryordering")
  @Enumerated(EnumType.STRING)
  @BeanProperty
  var folderOrdering: FolderOrdering = FolderOrdering.Alphabetical

  def fullName(folder: Folder, count: Int): String =
    Option(folder.parent) match {
      case None => folder.name
      case Some(p) => count match {
        case 0 => folder.name
        case _ => fullName(p, count - 1) + " > " + folder.name
      }
    }

  def fullName: String = fullName(this, 10)

  @BeanProperty var dateCreated: java.lang.Long = _
  @BeanProperty var dateUpdated: java.lang.Long = _

  @Column(name = "footer", length = 100000)
  @BeanProperty
  var footer: String = _

  @Column(name = "header", length = 100000)
  @BeanProperty
  var header: String = _

  @Column(name = "forwardUrl")
  @BeanProperty
  var redirect: String = _

  @BeanProperty
  var position: Int = _

  @Column(name = "hidden", nullable = false)
  @BeanProperty
  var hidden: Boolean = false

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  @BeanProperty var sections: java.util.Set[Section] = new util.HashSet[Section]()

  override def equals(obj: scala.Any): Boolean = obj match {
    case f: Folder => id == f.id
    case _ => false
  }

  override def hashCode(): Int = id.toInt
}

object Folder {
  def apply(root: Folder) = {

    val folder = new Folder
    folder.name = "new folder"
    folder.parent = root

    val section1 = new SubfolderSection
    section1.name = "subfolders"
    section1.folder = folder
    section1.visible = true

    val section2 = new FolderContentSection
    section2.name = "content"
    section2.folder = folder
    section2.visible = true

    val section3 = new ItemListSection
    section3.name = "items"
    section3.folder = folder
    section3.visible = true

    folder.sections.add(section1)
    folder.sections.add(section2)
    folder.sections.add(section3)

    folder.dateCreated = new DateTime(DateTimeZone.UTC).getMillis
    folder.dateUpdated = folder.dateCreated
    folder
  }
}
