package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.obj.Obj
import org.joda.time.Duration

/** @author Stephen Samuel */
class Listing(obj: Obj) {
  def expiry: Duration = new Duration(System.currentTimeMillis() - obj.expiry)
}
