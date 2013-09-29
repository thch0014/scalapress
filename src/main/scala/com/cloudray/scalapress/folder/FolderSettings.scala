package com.cloudray.scalapress.folder

import javax.persistence._
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}
import com.cloudray.scalapress.theme.Markup
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_category")
class FolderSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var sort: Sort = Sort.Name

    @BeanProperty var pageSize: Int = _
    @BeanProperty var header: String = _
    @BeanProperty var footer: String = _

    @ManyToOne
    @JoinColumn(name = "subfolderMarkup", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var subfolderMarkup: Markup = _
}
