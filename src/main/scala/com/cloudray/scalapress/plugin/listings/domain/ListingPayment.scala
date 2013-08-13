package com.cloudray.scalapress.plugin.listings.domain

import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel
  *
  *         A Listing Payment is the model used by the listing payment/renewal controller
  **/
class ListingPayment extends Serializable {
  var obj: Obj = _
  var listingPackage: ListingPackage = _
}
