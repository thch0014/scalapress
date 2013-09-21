package com.cloudray.scalapress.plugin.payments.sagepayform

import org.apache.commons.codec.binary.Base64
import java.util

/** @author Stephen Samuel */
object test extends App {

  val base642 = "BgMqFlpBEwoqCS8kVgoECTUUaUMFAXBAXkAdETgRDiQ0Wz9EdHJxNixVCGwpc09ZfVEFSgUecjAoJWZ4XQFCKBZXBUoMC3MPTzU/IB8wBVAfLWIhQVIzBxoiLjUKLBpQYFZ0QhUJZyYBA2sAHjEeAiIPNxNBWigcSREqMksWAw4zAzcBU0YrXE8yMwAeMR4jP1t8QAQAdkRdUnNnKhMlLgZUeSFwcBIgIDISYSgKMihwKwUmdntnPScqEmcqIRIfNRU3IFBAMh4dWwUOPwg3ORMuATYTYygBHSUkJQ4XEx4lCjBPeHITMSEjD2coE0Q/NRUxHkEOCjM9JQMEL2MxBDYSBRtRDndUWiIYJAgwBAgDEiUGQEB6PSJACAA9E0ssEScGMAB1HREoJwoAKSgvATwKMzN0cgYzKCd2ZygkBAkEHzQXCGUOIShAByAYMUIpOQEtBkYOd0RcXm0ABioDAyRbcUsbBHc="
  val base64 = "OxweBRwBIxcxCwkcTS4BFxIdX1VdS0dTRFUhPyEwFTAUXAhFNi5EIChKM0wxRU5bX1MsQUBMRjE2LF9dWz1EJDVCNldLXFkEVjIHEgMaAVkiMlYyBxIDGgEgCA0RCB9OR19CVE1DUDUbFlcuBxAFFgIIABIDBh0KTQ4RElMgAgwRAR4KFhQfXVE7CiUYDRgvHE5PXUNXXE9EVUtVNjkhJztLTTI2MCI9OzA0WTMuNzZXIjMwLjFQLj0/LkkzAAkLFRIAIRIcBwgZRD4uJz42OzEsKD1WMRwAAywdAAgrFRIGHwNSPyU5OjgkN1U0OUA2CAoFDQdOOi4mJyU8NEc0GhEbMw0JREBHQDckChERHxwjFRIHAhxPKyZfMyAlJUouMyUvO0UnKRA2LjMlLxQpDR8fAC4zJSw4MSBOVTQOAAA5AAAETiU+PDNCIRgDFUc3HggbEB5EQFdGS1EuHwsYFwRcRkpZWEI="
  val encrypted = Base64.decodeBase64(base64.getBytes)
  val x = xor(encrypted, "mypassword")
  println(new String(x))

  val input = "VendorTxCode=Order-102727&VPSTxId={6AA6DE3C-B694-7A80-5BAC-96D4EF1A8984}&Status=OK&StatusDetail=0000 : The Authorisation was Successful.&TxAuthNo=821316448&AVSCV2=SECURITY CODE MATCH ONLY&AddressResult=NOTMATCHED&PostCodeResult=MATCHED&CV2Result=MATCHED&GiftAid=0&3DSecureStatus=OK&CAVV=AAABB5FZcAAAABmYlllwAAAAAAA=&CardType=VISA&Last4Digits=0658&Amount=59.70"
  val xyz = xor(input.getBytes, "mypassword")
  val decrypted = Base64.encodeBase64(xyz)
  println(new String(decrypted))

  def xor(in: Array[Byte], key: String) = {
    val result = new Array[Byte](in.length)
    for ( k <- 0 until in.length ) {
      val b = in(k) ^ key.charAt(k % key.length)
      result(k) = b.toByte
    }
    result
  }
}
