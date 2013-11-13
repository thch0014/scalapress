package com.cloudray.scalapress.account

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.springframework.mail.{SimpleMailMessage, MailSender}
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.mockito.{Matchers, ArgumentCaptor, Mockito}

/** @author Stephen Samuel */
class PasswordServiceTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val accountDao = mock[AccountDao]
  val tokenDao = mock[PasswordTokenDao]
  val mailSender = mock[MailSender]
  val installationDao = mock[InstallationDao]
  val passwordEncoder = mock[PasswordEncoder]
  val tokenGenerator = mock[TokenGenerator]
  val passwordGenerator = mock[PasswordGenerator]

  val installation = new Installation
  installation.domain = "coldplay.com"
  Mockito.when(installationDao.get).thenReturn(installation)

  val tokenString = "adaoiiquwhriqwrid9gfu234ju9fj89jas89as"
  Mockito.when(tokenGenerator.generate).thenReturn(tokenString)

  val token = new PasswordToken
  token.token = tokenString

  val service = new PasswordService(accountDao,
    tokenDao,
    mailSender,
    installationDao,
    passwordEncoder,
    tokenGenerator,
    passwordGenerator)

  val account = new Account()
  account.email = "sammy@sam.com"

  Mockito.when(passwordEncoder.encodePassword(Matchers.anyString, Matchers.anyString)).thenReturn("hash123")
  Mockito.when(passwordGenerator.generate).thenReturn("simplepass")

  Mockito.when(accountDao.byEmail("sammy@sam.com")).thenReturn(Some(account))
  Mockito.when(tokenDao.find("sammy@sam.com", tokenString)).thenReturn(Some(token))

  "a password service" should "send out email for a password reset request to the account email" in {
    service.request(account)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    assert("sammy@sam.com" === captor.getValue.getTo()(0))
  }

  it should "include the generated token string parameter in the request email" in {
    service.request(account)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    assert(captor.getValue.getText.contains("token=" + tokenString))
  }

  it should "include the account email parameter in the request email" in {
    service.request(account)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    assert(captor.getValue.getText.contains("email=" + account.email))
  }

  it should "update the password and persist it on a reset" in {
    service.reset(tokenString, "sammy@sam.com")
    val captor = ArgumentCaptor.forClass(classOf[Account])
    Mockito.verify(accountDao).save(captor.capture)
    assert(captor.getValue.passwordHash === "hash123")
  }

  it should "send an email with the updated password" in {
    service.reset(tokenString, "sammy@sam.com")
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(mailSender).send(captor.capture)
    assert(captor.getValue.getText.contains("simplepass"))
  }

  it should "send no emails for invalid token" in {
    service.reset("invalidtoken", "sammy@sam.com")
    Mockito.verify(mailSender, Mockito.never).send(Matchers.any[SimpleMailMessage])
  }

  it should "persist nothing for invalid token" in {
    service.reset("invalidtoken", "sammy@sam.com")
    Mockito.verify(accountDao, Mockito.never).save(Matchers.any[Account])
  }

  it should "send no emails for invalid email" in {
    service.reset(tokenString, "samm22222y@sam.com")
    Mockito.verify(mailSender, Mockito.never).send(Matchers.any[SimpleMailMessage])
  }

  it should "persist nothing for invalid email" in {
    service.reset(tokenString, "sammy2222@sam.com")
    Mockito.verify(accountDao, Mockito.never).save(Matchers.any[Account])
  }
}
