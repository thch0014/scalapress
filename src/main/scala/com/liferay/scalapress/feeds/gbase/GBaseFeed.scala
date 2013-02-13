package com.liferay.scalapress.feeds.gbase

import javax.persistence.{Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.feeds.Feed

/** @author Stephen Samuel */
@Entity
@Table(name = "feeds_gbase")
class GBaseFeed extends Feed {

    @BeanProperty var ftpHostname: Long = _
    @BeanProperty var ftpUsername: Long = _
    @BeanProperty var ftpPassword: Long = _
    @BeanProperty var googleProductCategory: Long = _

    def backoffice: String = "/backoffice/feed/gbase/" + id
}
