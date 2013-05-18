package com.liferay.scalapress.folder

import javax.persistence.{GenerationType, GeneratedValue, Id, Entity, Table}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_category")
class FolderSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var pageSize: Int = _
    @BeanProperty var header: String = _
    @BeanProperty var footer: String = _
}
