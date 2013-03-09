package com.liferay.scalapress.widgets

import javax.persistence.{JoinColumn, JoinTable, CascadeType, FetchType, ManyToMany, Column, GenerationType, GeneratedValue, Id, InheritanceType, Inheritance, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.domain.{WidgetContainer, Folder}
import scala.Array

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

    def render(req: ScalapressRequest, context: ScalapressContext): Option[String]
    def widgetType: String = getClass.getSimpleName

    @Column(name = "cssId")
    @BeanProperty var containerId: String = _

    @Column(name = "cssClass")
    @BeanProperty var containerClass: String = _

    @BeanProperty var position: Int = _

    @Column(name = "content")
    @BeanProperty var content: String = _

    @BeanProperty var name: String = _

    @BeanProperty var location: String = _

    @BeanProperty var visible: Boolean = _

    @Column(name = "displayOnAllCategories")
    @BeanProperty var displayOnAllFolders: Boolean = _

    @Column(name = "displayOnAllItems")
    @BeanProperty var displayOnAllObjects: Boolean = _

    @BeanProperty var displayOnHome: Boolean = _
    @BeanProperty var displayOnOthers: Boolean = _

    //    @Column(name = "displayOnAllItems")
    // @BeanProperty var displayOnBasket: Boolean = _

    //  @Column(name = "displayOnAllItems")
    //   @BeanProperty var displayOnCheckout: Boolean = _

    @Column(name = "restricted", nullable = false, columnDefinition = "tinyint(1) not null default 0")
    @BeanProperty var restricted: Boolean = _

    @Column(name = "migrated", nullable = false, columnDefinition = "tinyint(1) not null default 0")
    @BeanProperty var migrated: Boolean = _

    @ManyToMany(fetch = FetchType.EAGER, cascade = Array(CascadeType.ALL))
    @JoinTable(name = "boxes_where",
        joinColumns = Array(new JoinColumn(name = "box", unique = true)),
        inverseJoinColumns = Array(new JoinColumn(name = "category"))
    )
    @BeanProperty var whichFolders: java.util.List[Folder] = new java.util.ArrayList[Folder]

    /**
     * When set to true the selected categories will be EXCLUDED and all else will be visable, rather than normally the list is inclusive.
     */
    // private var excludeCategories: Boolean = false

    /**
     * Show this box on items inside the specified categories
     */
    // private var displayOnItemsInCategories: Boolean = false

    /**
     * Show this sidebox on all other pages not covered
     */
    //

    // @BeanProperty var disabled: Boolean = false

    // protected var markup: Markup = null

    // control who sees this widget
    //    @BeanProperty var who: Boolean = _
    // control on which folders the widget is shown
    //

    //
    //

    // @BeanProperty var displayToGuests: Boolean = _
    //
    // @BeanProperty var displayOnSearchResults: Boolean = _

    /**
     * display when a user is logged in regardless of type
     */
    // private var displayToAllAccounts: Boolean = false

}
