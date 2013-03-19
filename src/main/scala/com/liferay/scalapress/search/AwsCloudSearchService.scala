package com.liferay.scalapress.search

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.cloudsearch.AmazonCloudSearchClient
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
class AwsCloudSearchService(accessKey: String, secretKey: String) {

    def getCloudSearchClient: AmazonCloudSearchClient = {
        val cred = new BasicAWSCredentials(accessKey, secretKey)
        new com.amazonaws.services.cloudsearch.AmazonCloudSearchClient(cred)
    }

    def index(obj: Obj) {

    }
}
