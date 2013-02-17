package com.liferay.scalapress

import java.net.URL
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class UriBuilder(protocol: Option[String],
                 hostname: Option[String],
                 port: Option[Int],
                 path: Option[String],
                 map: Map[String, String]) {

    def ?(kv: (String, Any)) = this - (kv._1) & kv
    def &(kv: (String, Any)) = {
        val param = (kv._1, kv._2.toString)
        new UriBuilder(protocol, hostname, port, path, map + param)
    }

    def -(key: (String)) = new UriBuilder(protocol, hostname, port, path, map - key)
    override def toString = {
        val sb = new StringBuilder
        sb append protocol.getOrElse("http") append ("://")
        sb append hostname.getOrElse("") append (":")
        sb append port.getOrElse("80")
        sb append path.getOrElse("") append "?"
        map.foreach(arg => {
            sb.append(arg._1 + "=" + arg._2)
            sb.append("&")
        })
        sb.toString()
    }
}

object UriBuilder {
    def apply = new UriBuilder(None, None, None, None, Map.empty)
    def apply(req: HttpServletRequest): UriBuilder = apply(req
      .getRequestURL
      .append("?")
      .append(Option(req.getQueryString).getOrElse(""))
      .toString)
    def apply(url: String) = {
        val u = new URL(url)
        val params: Map[String, String] = Option(u.getQuery)
          .getOrElse("")
          .split("&")
          .map(_.split("="))
          .filter(_.length == 2)
          .map(a => (a(0), a(1)))
          .toMap
        new UriBuilder(Option(u.getProtocol),
            Option(u.getHost),
            Option(u.getPort).filter(_ < 0),
            Option(u.getPath),
            params)
    }
}
