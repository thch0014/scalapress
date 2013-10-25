package com.cloudray.scalapress.account

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.cloudray.scalapress.media.ImageDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("rest/account"))
class AccountRestController(accountDao: AccountDao,
                            imageDao: ImageDao) {

  @ResponseBody
  @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def typeAhead(@RequestParam("q") q: String): Array[Array[String]] = accountDao.typeAhead(q)
}
