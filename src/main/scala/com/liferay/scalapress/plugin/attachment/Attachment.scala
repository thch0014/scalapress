package com.liferay.scalapress.plugin.attachment

import scala.Predef.String
import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity, JoinColumn, ManyToOne}
import reflect.BeanProperty
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
@Entity
@Table(name = "attachments")
class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "folder")
    @BeanProperty var folder: Folder = _

    @ManyToOne
    @JoinColumn(name = "obj")
    @BeanProperty var obj: Obj = _

    // link to the file in the asset store
    @BeanProperty var filename: String = _

    // proper name of this file, used for display to humans
    @BeanProperty var name: String = _

    // a full description of the upload
    @BeanProperty var description: String = _

}
