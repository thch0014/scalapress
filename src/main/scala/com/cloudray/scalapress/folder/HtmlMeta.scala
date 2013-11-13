package com.cloudray.scalapress.folder

import scala.beans.BeanProperty
import javax.persistence.Column

/** @author Stephen Samuel */
trait HtmlMeta {

  @Column(name = "titleTag", length = 1000)
  @BeanProperty var titleTag: String = _

  @Column(name = "descriptionTag", length = 1000)
  @BeanProperty var descriptionTag: String = _

  @Column(name = "keywords", length = 1000)
  @BeanProperty var keywordsTag: String = _
}
