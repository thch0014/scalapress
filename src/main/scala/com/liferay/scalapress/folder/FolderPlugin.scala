package com.liferay.scalapress.folder

import javax.persistence.{GenerationType, GeneratedValue, Id, Entity, Table}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_category")
class FolderPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var header: String = _
    @BeanProperty var footer: String = _
}
