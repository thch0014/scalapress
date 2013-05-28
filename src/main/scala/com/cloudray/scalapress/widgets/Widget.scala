package com.cloudray.scalapress.widgets

import javax.persistence._
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.enums.WidgetContainer
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
abstract class Widget {

    final def init(context: ScalapressContext) {
        this.location = "Left"
        _init(context)
        context.widgetDao.save(this)
    }

    def _init(context: ScalapressContext) {}

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

    @Column(name = "content", length = 100000)
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

    @BeanProperty var excludeFolders: String = _

    @BeanProperty var includeFolders: String = _

    //    @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
    //    @JoinTable(name = "boxes_where",
    //        joinColumns = Array(new JoinColumn(name = "box", unique = true)),
    //        inverseJoinColumns = Array(new JoinColumn(name = "category"))
    //    )
    //    @Fetch(FetchMode.SUBSELECT)
    //    @BatchSize(size = 20)
    //    @BeanProperty var whichFolders: java.util.Set[Folder] = new java.util.HashSet[Folder]
}