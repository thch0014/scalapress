package com.liferay.scalapress.plugin.form

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.util.EntityUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair
import java.util
import org.apache.http.NameValuePair

/** @author Stephen Samuel */
object RecaptchaClient {

    def post(recaptchaChallenge: String, recaptchaResponse: String, ipaddress: String): Boolean = {

        val post = new HttpPost("http://www.google.com/recaptcha/api/verify")

        val nameValuePairs = new util.ArrayList[NameValuePair]()
        nameValuePairs.add(new BasicNameValuePair("privatekey", "6LeFAt0SAAAAAPIxED6O_TlpcRS66wrZc0NEva_s"))
        nameValuePairs.add(new BasicNameValuePair("remoteip", ipaddress))
        nameValuePairs.add(new BasicNameValuePair("challenge", recaptchaChallenge))
        nameValuePairs.add(new BasicNameValuePair("response", recaptchaResponse))
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs))

        val client = new DefaultHttpClient
        val resp = client.execute(post)
        EntityUtils.toString(resp.getEntity) == "true"
    }
}
