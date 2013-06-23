package com.cloudray.scalapress.search

import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.enums.Sort

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_search")
class SearchSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var sort: Sort = Sort.Name
    @BeanProperty var pageSize: Int = _

    @BeanProperty var header: String = _
    @BeanProperty var footer: String = _
}
