package com.liferay.scalapress.plugin.profile

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.settings.InstallationDao
import com.liferay.scalapress.obj.ObjectDao
import scala.util.Random
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.sksamuel.scoot.rest.Logging
import org.apache.commons.lang.RandomStringUtils

/** @author Stephen Samuel */
@Component
class PasswordService extends Logging {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var passwordTokenDao: PasswordTokenDao = _
    @Autowired var mailSender: MailSender = _
    @Autowired var installationDao: InstallationDao = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    def request(email: String) {

        objectDao.byEmail(email) match {
            case None => logger.debug("No account found [{}]", email)
            case Some(account) =>

                val pt = new PasswordToken
                pt.email = email
                pt.token = RandomStringUtils.randomAlphanumeric(8)
                passwordTokenDao.save(pt)
                logger.debug("Token generated [{},{}]", pt.email, pt.token)

                val domain = installationDao.get.domain
                val body = "A message from the admin at " + domain + ".\n\nSomeone requested that the password for " + email + " be reset.\n.If this was not you then you can ignore this email.\nIf it was you then click the following link to reset your password:\nhttp://" + domain + "/password?token=" + pt
                  .token + "&email=" + email

                val msg = new SimpleMailMessage
                msg.setTo(email)
                msg.setFrom("donotreply@" + installationDao.get.domain)
                msg.setText(body)
                msg.setSubject("Password reset request")
                mailSender.send(msg)
        }
    }

    def reset(token: String, email: String): Boolean = {
        passwordTokenDao.find(email, token) match {
            case None => false
            case Some(pt) =>

                val domain = installationDao.get.domain
                val password = RandomStringUtils.randomAlphanumeric(8)

                val account = objectDao.byEmail(email).get
                account.passwordHash = passwordEncoder.encodePassword(password, null)
                logger.debug("Saving account with updated password")
                objectDao.save(account)

                val body = "A message from the admin at " + domain + ".\n\nYour password has been reset to:\n" + password

                val msg = new SimpleMailMessage
                msg.setTo(email)
                msg.setFrom("donotreply@" + installationDao.get.domain)
                msg.setText(body)
                msg.setSubject("Password reset")
                mailSender.send(msg)

                logger.debug("Deleting password token")
                passwordTokenDao.remove(pt)

                true
        }
    }
}
