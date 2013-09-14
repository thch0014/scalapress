package com.cloudray.scalapress.plugin.form

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.apache.commons.io.IOUtils
import org.apache.http.client.HttpClient
import org.mockito.{Matchers, Mockito}
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.HttpResponse
import org.apache.http.entity.StringEntity

/** @author Stephen Samuel */
class RecaptchaClientTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val http = mock[HttpClient]
  val client = new RecaptchaClient(http)

  "a recaptcha client" should "use challenge response when creating POST" in {
    val post = client.createPost("chall", "bigresp", "1.2.3.4")
    assert(IOUtils.toString(post.getEntity.getContent).contains("challenge=chall"))
  }

  "a recaptcha client" should "use ip address when creating POST" in {
    val post = client.createPost("chall", "bigresp", "1.2.3.4")
    assert(IOUtils.toString(post.getEntity.getContent).contains("remoteip=1.2.3.4"))
  }

  "a recaptcha client" should "use response when creating POST" in {
    val post = client.createPost("chall", "bigresp", "1.2.3.4")
    assert(IOUtils.toString(post.getEntity.getContent).contains("response=bigresp"))
  }

  "a recaptcha client" should "return true if the outcome is true" in {
    val resp = mock[HttpResponse]
    Mockito.when(resp.getEntity).thenReturn(new StringEntity("true"))
    Mockito.when(http.execute(Matchers.any[HttpUriRequest])).thenReturn(resp)
    assert(client.post("", "", ""))
  }

  "a recaptcha client" should "return false if the outcome is not equal to true" in {
    val resp = mock[HttpResponse]
    Mockito.when(resp.getEntity).thenReturn(new StringEntity("2"))
    Mockito.when(http.execute(Matchers.any[HttpUriRequest])).thenReturn(resp)
    assert(!client.post("", "", ""))
  }
}
