package com.cloudray.scalapress.framework

/**
 * Reg links are shown on the registration completion page.
 *
 * @author Stephen Samuel */
trait RegistrationLinkProvider {

  def enabled(context: ScalapressContext): Boolean
  def text: String
  def description: String
  def link: String
}
