package com.cloudray.scalapress.media

import javax.persistence.Embeddable

/**
 * General purpose embeddable image construct. Useful where the
 * attached images to an entity need more context, such as a description.
 *
 * @author Stephen Samuel */
@Embeddable
case class Image(var key: String, var description: String) {
  def this() = this(null, null)
}
