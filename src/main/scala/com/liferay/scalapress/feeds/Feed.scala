package com.liferay.scalapress.feeds

import javax.persistence.{GenerationType, GeneratedValue, Id, InheritanceType, Inheritance, Entity}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @BeanProperty var id: Long = _

    @BeanProperty var lastRuntime: Long = _

    def feedType = getClass.getSimpleName

    def backoffice: String
}
