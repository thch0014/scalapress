package com.liferay.scalapress.section

import javax.persistence._
import reflect.BeanProperty
import org.hibernate.annotations.{CacheConcurrencyStrategy, Index}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.obj.{ObjectType, Obj}
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
abstract class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @BeanProperty var id: Long = _

    @Column(name = "visible")
    @BeanProperty var visible: Boolean = _

    @Column(name = "name")
    @BeanProperty var name: String = _

    @Column(name = "position")
    @BeanProperty var position: Int = _

    @ManyToOne
    @JoinColumn(name = "ownerCategory", nullable = true)
    @Index(name = "folder_index")
    @BeanProperty var folder: Folder = _

    @ManyToOne
    @JoinColumn(name = "ownerItem", nullable = true)
    @Index(name = "object_index")
    @BeanProperty var obj: Obj = _

    @ManyToOne
    @JoinColumn(name = "ownerItemType", nullable = true)
    @Index(name = "objecttype_index")
    @BeanProperty var objectType: ObjectType = _

    def desc: String
    def render(request: ScalapressRequest, context: ScalapressContext): Option[String]
    def backoffice: String = "/backoffice/section/" + id

    final def init(context: ScalapressContext) {
        _init(context)
        context.sectionDao.save(this)
    }
    def _init(context: ScalapressContext) {}
}
