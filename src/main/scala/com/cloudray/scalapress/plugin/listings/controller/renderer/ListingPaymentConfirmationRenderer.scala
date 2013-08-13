package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.plugin.listings.domain.{ListingPayment, ListingProcess}

/** @author Stephen Samuel */
class ListingPaymentConfirmationRenderer {

  def render(listingPayment: ListingPayment) = {

    <div id="listing-process-confirmation">
      <legend>
        Listing Confirmation
      </legend>
      <table class="table table-hover table-condensed">
        <tr>
          <td>
            <b>Title</b>
          </td>
          <td>
            {listingPayment.obj.title}
          </td>
        </tr>
        <tr>
          <td>
            <b>Package</b>
          </td>
          <td>
            {listingPayment.listingPackage.name}&nbsp;{listingPayment.listingPackage.priceText}
          </td>
        </tr>
      </table>
    </div>
  }

  def _complete(process: ListingProcess) = {
    <form method="POST" action="/listing/confirmation">
      <button type="submit" class="btn">
        Complete
      </button>
    </form>
  }
}
