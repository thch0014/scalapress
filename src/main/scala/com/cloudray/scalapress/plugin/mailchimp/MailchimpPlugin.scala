package com.cloudray.scalapress.plugin.mailchimp

import com.ecwid.mailchimp.method.list.EmailType

/** @author Stephen Samuel */
class MailchimpPlugin(apiKey: String) {

    val client = new com.ecwid.mailchimp.MailChimpClient

    def send(subject: String, body: String) {
        val method = new com.ecwid.mailchimp.method.list.ListSubscribeMethod
        method.apikey = apiKey
        method.email_type = EmailType.html
        client.execute(method)
    }

    def subscribe(email: String) {
        val method = new com.ecwid.mailchimp.method.list.ListSubscribeMethod
        method.email_address = email
        method.apikey = apiKey
        method.email_type = EmailType.html
        method.send_welcome = true
        client.execute(method)
    }

    def unsubscribe(email: String) {
        val method = new com.ecwid.mailchimp.method.list.ListUnsubscribeMethod
        method.email_address = email
        method.apikey = apiKey
        client.execute(method)
    }
}
