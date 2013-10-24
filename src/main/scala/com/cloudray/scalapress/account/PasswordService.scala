package com.cloudray.scalapress.account

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.settings.InstallationDao
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.sksamuel.scoot.rest.Logging
import org.apache.commons.lang.RandomStringUtils

/** @author Stephen Samuel */
@Component
@Autowired
class PasswordService(accountDao: AccountDao,
                      passwordTokenDao: PasswordTokenDao,
                      mailSender: MailSender,
                      installationDao: InstallationDao,
                      passwordEncoder: PasswordEncoder,
                      tokenGenerator: TokenGenerator,
                      passwordGenerator: PasswordGenerator) extends Logging {

  def request(email: String) {
    accountDao.byEmail(email) match {
      case Some(account) => request(account)
      case _ => logger.debug("No account found [{}]", email)
    }
  }

  def generateToken(account: Account) = {
    val pt = new PasswordToken
    pt.email = account.email
    pt.token = tokenGenerator.generate
    passwordTokenDao.save(pt)
    logger.debug("Token generated [{},{}]", pt.email, pt.token)
    pt
  }

  def request(account: Account) {

    val token = generateToken(account)
    val domain = installationDao.get.domain
    val from = s"donotreply@$domain"

    val msg = new SimpleMailMessage
    msg.setTo(account.email)
    msg.setFrom(from)
    msg.setText(requestBody(account, token))
    msg.setSubject("Password reset request")
    mailSender.send(msg)
  }

  def requestBody(account: Account, token: PasswordToken): String = {
    val domain = installationDao.get.domain
    s"A message from the admin at $domain.\n\n" +
      s"Someone requested that the password for ${account.email} be reset.\n\n" +
      s"If this was not you then you can ignore this email.\n\n" +
      s"If it was you then click the following link to reset your password:\n" +
      s"http://$domain/password?token=${token.token}&email=${account.email}"
  }

  def reset(token: String, email: String): Boolean = {
    accountDao.byEmail(email) match {
      case Some(account) => reset(token, account)
      case _ => false
    }
  }

  def reset(token: String, account: Account): Boolean = {
    passwordTokenDao.find(account.email, token) match {
      case Some(pt) => reset(pt, account)
      case _ => false
    }
  }

  def reset(token: PasswordToken, account: Account): Boolean = {

    val domain = installationDao.get.domain
    val password = passwordGenerator.generate

    val body = resetBody(domain, password)

    account.passwordHash = passwordEncoder.encodePassword(password, null)
    logger.debug("Saving account with updated password")
    accountDao.save(account)

    val msg = new SimpleMailMessage
    msg.setTo(account.email)
    msg.setFrom("donotreply@" + installationDao.get.domain)
    msg.setText(body)
    msg.setSubject("Password reset")
    mailSender.send(msg)

    logger.debug("Deleting password token")
    passwordTokenDao.remove(token)

    true
  }

  def resetBody(domain: String, password: String): String = {
    s"A message from the admin at $domain.\n\n" +
      s"Your password has been reset to:\n" +
      s"$password\n\n" +
      s"You can login to your account here:\n" +
      s"http://$domain/login"
  }
}

trait TokenGenerator {
  def generate: String
}

@Component
class AlphanumericTokenGenerator extends TokenGenerator {
  override def generate = RandomStringUtils.randomAlphanumeric(8)
}

trait PasswordGenerator {
  def generate: String
}

@Component
class AlphanumericPasswordGenerator extends PasswordGenerator {
  override def generate = RandomStringUtils.randomAlphanumeric(8)
}
