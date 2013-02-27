package com.liferay.scalapress.widgets

import javax.persistence.{JoinColumn, JoinTable, CascadeType, FetchType, ManyToMany, Column, GenerationType, GeneratedValue, Id, InheritanceType, Inheritance, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.domain.Folder
import scala.Array
import com.liferay.scalapress.enums.WidgetContainer

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @BeanProperty var id: Long = _

    @BeanProperty var container: WidgetContainer = WidgetContainer.Table

    @Column(name = "captionText")
    @BeanProperty var caption: String = _

    def render(req: ScalapressRequest): Option[String]
    def widgetType: String = getClass.getSimpleName
    def backoffice = "/backoffice/widget/" + id

    @Column(name = "cssId")
    @BeanProperty var containerId: String = _

    @Column(name = "cssClass")
    @BeanProperty var containerClass: String = _

    @BeanProperty var position: Int = _

    @Column(name = "content")
    @BeanProperty var content: String = _

    @BeanProperty var name: String = _

    @BeanProperty var location: String = _

    @Column(name = "visible", nullable = false)
    @BeanProperty var visible: Boolean = _

    @Column(name = "displayOnAllCategories")
    @BeanProperty var displayOnAllFolders: Boolean = _

    @Column(name = "displayOnAllItems")
    @BeanProperty var displayOnAllObjects: Boolean = _

    @Column(name = "displayOnHome", nullable = false)
    @BeanProperty var displayOnHome: Boolean = _

    @Column(name = "displayOnOthers", nullable = false)
    @BeanProperty var displayOnOthers: Boolean = _

    @Column(name = "displayOnSearchResults", nullable = false)
    @BeanProperty var displayOnSearchResults: Boolean = _

    @Column(name = "displayOnBasket")
    @BeanProperty var displayOnBasket: Boolean = _

    @Column(name = "displayOnCheckout")
    @BeanProperty var displayOnCheckout: Boolean = _

    @Column(name = "restricted", nullable = false)
    @BeanProperty var restricted: Boolean = _

    @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
    @JoinTable(name = "boxes_where",
        joinColumns = Array(new JoinColumn(name = "box", unique = true)),
        inverseJoinColumns = Array(new JoinColumn(name = "category"))
    )
    @BeanProperty var whichFolders: java.util.Set[Folder] = new java.util.HashSet[Folder]
}
