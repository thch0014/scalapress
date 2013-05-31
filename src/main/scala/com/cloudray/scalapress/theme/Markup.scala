package com.cloudray.scalapress.theme

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "markup")
class Markup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Column(name = "name", length = 256)
    @BeanProperty var name: String = _

    @Column(name = "body", length = 100000)
    @BeanProperty var body: String = _

    @Column(name = "start", length = 100000)
    @BeanProperty var start: String = _

    @Column(name = "end", length = 100000)
    @BeanProperty var end: String = _

    @Column(name = "between", length = 100000)
    @BeanProperty var between: String = _

}
