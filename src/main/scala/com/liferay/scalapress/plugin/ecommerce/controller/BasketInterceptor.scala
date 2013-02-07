package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.scalapress.controller.web.ScalapressConstants
import com.liferay.scalapress.dao.ecommerce.BasketDao

/** @author Stephen Samuel */
class BasketInterceptor(basketDao: BasketDao) extends HandlerInterceptorAdapter {

    override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {

        Option(request.getAttribute(ScalapressConstants.SessionIdKey)).map(_.asInstanceOf[String]) match {
            case None => None
            case Some(sessionId) =>
                val basket = basketDao.find(sessionId)
                request.setAttribute(ScalapressConstants.BasketKey, basket)
        }

        true
    }
}
