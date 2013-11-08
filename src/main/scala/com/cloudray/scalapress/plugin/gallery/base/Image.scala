package com.cloudray.scalapress.plugin.gallery.base

import javax.persistence.Embeddable

/**
 * General purpose embeddable image construct. Useful where the
 * attached images to an entity need more context, such as a description.
 *
 * @author Stephen Samuel */
@Embeddable
case class Image(key: String, description: String, date: Long = System.currentTimeMillis) {
  def this() = this(null, null, System.currentTimeMillis)
}