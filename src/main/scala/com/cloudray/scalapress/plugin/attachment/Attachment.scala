package com.cloudray.scalapress.plugin.attachment

import javax.persistence._

/** @author Stephen Samuel */
@Embeddable
case class Attachment(var assetKey: String, var description: String) {
  def this() = this(null, null)
}