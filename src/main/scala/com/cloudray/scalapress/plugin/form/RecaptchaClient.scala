package com.cloudray.scalapress.plugin.form

import org.apache.http.client.methods.HttpPost
import org.apache.http.util.EntityUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import java.util
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient

/** @author Stephen Samuel */
class RecaptchaClient(client: HttpClient) {

  val URL = "http://www.google.com/recaptcha/api/verify"

  def post(recaptchaChallenge: String, recaptchaResponse: String, ipaddress: String): Boolean = {
    val post = createPost(recaptchaChallenge, recaptchaResponse, ipaddress)
    val resp = client.execute(post)
    val outcome = EntityUtils.toString(resp.getEntity)
    outcome.toLowerCase.startsWith("true")
  }

  def createPost(recaptchaChallenge: String, recaptchaResponse: String, ipaddress: String): HttpPost = {
    val nameValuePairs = new util.ArrayList[NameValuePair]()
    nameValuePairs.add(new BasicNameValuePair("privatekey", "6LeFAt0SAAAAAPIxED6O_TlpcRS66wrZc0NEva_s"))
    nameValuePairs.add(new BasicNameValuePair("remoteip", ipaddress))
    nameValuePairs.add(new BasicNameValuePair("challenge", recaptchaChallenge))
    nameValuePairs.add(new BasicNameValuePair("response", recaptchaResponse))

    val post = new HttpPost(URL)
    post.setEntity(new UrlEncodedFormEntity(nameValuePairs))
    post
  }
}
