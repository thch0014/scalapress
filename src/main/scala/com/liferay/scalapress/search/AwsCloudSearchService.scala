package com.liferay.scalapress.search

import com.liferay.scalapress.domain.Obj
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.cloudsearch.AmazonCloudSearchClient

/** @author Stephen Samuel */
class AwsCloudSearchService(accessKey: String, secretKey: String) {

    def getCloudSearchClient: AmazonCloudSearchClient = {
        val cred = new BasicAWSCredentials(accessKey, secretKey)
        new com.amazonaws.services.cloudsearch.AmazonCloudSearchClient(cred)
    }

    def index(obj: Obj) {

    }
}
