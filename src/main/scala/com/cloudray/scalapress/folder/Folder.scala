package com.cloudray.scalapress.folder

import javax.persistence._
import java.util
import com.cloudray.scalapress.enums.FolderOrdering
import org.hibernate.annotations._
import com.cloudray.scalapress.section.{SortedSections, Section}
import com.cloudray.scalapress.obj.Obj
import section.{FolderContentSection, SubfolderSection}
import com.cloudray.scalapress.theme.Theme
import org.joda.time.{DateTime, DateTimeZone}
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.CascadeType
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import org.hibernate.annotations.Index

/** @author Stephen Samuel */
@Entity
@Table(name = "categories")
class Folder extends SortedSections {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "folders", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.SELECT)
    @BeanProperty var objects: java.util.Set[Obj] = new java.util.HashSet[Obj]()

    @Index(name = "parent_index")
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 40)
    @BeanProperty var subfolders: java.util.Set[Folder] = new java.util.HashSet[Folder]()

    def sortedSubfolders: Array[Folder] = subfolders.asScala.toArray.filterNot(_.name == null).sortBy(_.name).sortBy(_.position)

    @ManyToOne(cascade = Array(CascadeType.ALL))
    @JoinColumn(name = "parent")
    @Fetch(FetchMode.JOIN)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var parent: Folder = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme")
    @BeanProperty var theme: Theme = _

    @Index(name = "name_index")
    @BeanProperty var name: String = _

    @Column(name = "subcategoryordering")
    @Enumerated(EnumType.STRING)
    @BeanProperty var folderOrdering: FolderOrdering = FolderOrdering.Alphabetical

    def fullName(folder: Folder, count: Int): String = Option(folder.parent) match {
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
    @BeanProperty var footer: String = _

    @Column(name = "header", length = 100000)
    @BeanProperty var header: String = _

    @Column(name = "forwardUrl")
    @BeanProperty var redirect: String = _

    @BeanProperty var position: Int = _

    @BeanProperty var hidden: Boolean = false

    @BeanProperty var titleTag: String = _
    @BeanProperty var descriptionTag: String = _

    @Column(name = "keywords")
    @BeanProperty var keywordsTag: String = _

    @Column(name = "friendlyUrl")
    @BeanProperty var permaLink: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.SELECT)
    @BeanProperty var sections: java.util.Set[Section] = new util.HashSet[Section]()

}

object Folder {
    def apply(root: Folder) = {

        val folder = new Folder
        folder.name = "new folder"
        folder.parent = root

        val section1 = new FolderContentSection
        section1.name = "content"
        val section2 = new SubfolderSection
        section2.name = "subfolders"

        folder.sections.add(section2)
        folder.sections.add(section1)

        folder.dateCreated = new DateTime(DateTimeZone.UTC).getMillis
        folder.dateUpdated = new DateTime(DateTimeZone.UTC).getMillis
        folder
    }
}