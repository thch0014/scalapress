package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.obj.Item
import org.joda.time.Duration

/** @author Stephen Samuel */
class Listing(obj: Item) {
  def expiry: Duration = Duration.millis(System.currentTimeMillis() - obj.expiry)
}
