package com.cloudray.scalapress.plugin.compatibility

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, RequestMapping}
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@Autowired
class ECRedirectController(context: ScalapressContext) {

  @RequestMapping(Array("c.do"))
  def category(@RequestParam("category") id: Long) = {
    Option(context.folderDao.find(id)) match {
      case None => "redirect:/"
      case Some(folder) => "redirect:" + UrlGenerator.url(folder)
    }
  }

  @RequestMapping(Array("item.do"))
  def item(@RequestParam("item") id: Long) = {
    Option(context.objectDao.find(id)) match {
      case None => "redirect:/"
      case Some(obj) => "redirect:" + UrlGenerator.url(obj)
    }
  }

  @RequestMapping(Array("{bumf}-c{id:\\d+}.html"))
  def categoryHtml(@PathVariable("id") id: Long) = category(id)

  @RequestMapping(Array("{bumf}-i{id:\\d+}.html"))
  def itemHtml(@PathVariable("id") id: Long) = item(id)
}
