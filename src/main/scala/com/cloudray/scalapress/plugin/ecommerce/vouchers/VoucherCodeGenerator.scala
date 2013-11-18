package com.cloudray.scalapress.plugin.ecommerce.vouchers

import org.apache.commons.lang.RandomStringUtils
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
trait VoucherCodeGenerator {
  def generate: String
}

@Component
class RandomAlphanumericVoucherCodeGenerator extends VoucherCodeGenerator {
  def generate: String = RandomStringUtils.randomAlphanumeric(6).toLowerCase
}