package com.liferay.scalapress.section

import javax.persistence.{JoinColumn, ManyToOne, Entity, Inheritance, InheritanceType, Column, GenerationType, GeneratedValue, Id}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.domain.{ObjectType, Obj, Folder}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
    @BeanProperty var folder: Folder = _

    @ManyToOne
    @JoinColumn(name = "ownerItem", nullable = true)
    var obj: Obj = _

    @ManyToOne
    @JoinColumn(name = "ownerItemType", nullable = true)
    var objectType: ObjectType = _

    def desc: String
    def render(request: ScalapressRequest, context: ScalapressContext): Option[String]
}
