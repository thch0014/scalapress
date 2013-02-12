package com.liferay.scalapress.domain

import javax.persistence._
import java.util
import reflect.BeanProperty
import scala.Array
import com.liferay.scalapress.enums.FolderOrdering
import com.liferay.scalapress.plugin.folder.section.{FolderContentSection, SubfolderSection}
import com.liferay.scalapress.Section
import setup.Theme

/** @author Stephen Samuel */
@Entity
@Table(name = "categories")
class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "folders", cascade = Array(CascadeType.ALL))
    @BeanProperty var objects: java.util.List[Obj] = new java.util.ArrayList[Obj]()

    @OneToMany(mappedBy = "parent")
    @OrderBy("name")
    @BeanProperty var subfolders: java.util.List[Folder] = new java.util.ArrayList[Folder]()

    @ManyToOne(cascade = Array(CascadeType.ALL))
    @JoinColumn(name = "parent")
    @BeanProperty var parent: Folder = _

    @ManyToOne
    @JoinColumn(name = "theme")
    @BeanProperty var theme: Theme = _

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", cascade = Array(CascadeType.ALL))
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

    @BeanProperty var dateCreated: java.lang.Long = _
    @BeanProperty var dateUpdated: java.lang.Long = _

    @BeanProperty var footer: String = _
    @BeanProperty var header: String = _

    @BeanProperty var css: String = _

    @Column(name = "forwardUrl")
    @BeanProperty var redirect: String = _

    @BeanProperty var position: Int = _

    @BeanProperty var primaryParent: Long = _

    // hidden when browsing but visible in search
    @BeanProperty var hidden: Boolean = false

    @BeanProperty var content: String = _

    @BeanProperty var includeSubcategoryItems: Boolean = false

    // meta data
    @BeanProperty var titleTag: String = _
    @BeanProperty var descriptionTag: String = _

    @Column(name = "keywords")
    @BeanProperty var keywordsTag: String = _

    @Column(name = "friendlyUrl")
    @BeanProperty var permaLink: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", cascade = Array(CascadeType.ALL))
    @BeanProperty var sections: java.util.List[Section] = new util.ArrayList[Section]()

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

        folder.dateCreated = System.currentTimeMillis
        folder.dateUpdated = System.currentTimeMillis
        folder
    }
}
