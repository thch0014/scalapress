package com.liferay.scalapress.feeds.gbase

import javax.persistence.{Column, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.feeds.Feed

/** @author Stephen Samuel */
@Entity
@Table(name = "feeds_googlebase")
class GBaseFeed extends Feed {

    @BeanProperty var ftpHostname: String = _
    @BeanProperty var ftpUsername: String = _
    @BeanProperty var ftpPassword: String = _
    @BeanProperty var ftpFilename: String = _

    @BeanProperty var brandAttrName: String = _
    @BeanProperty var partAttrName: String = _

    @Column(name = "productType")
    @BeanProperty var productCategory: String = _

    def backoffice: String = "/backoffice/feed/gbase/" + id
    def runUrl: String = "/backoffice/feed/gbase/" + id + "/run"
}
