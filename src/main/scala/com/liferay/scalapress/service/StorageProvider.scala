package com.liferay.scalapress.service

import java.io.InputStream
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.AmazonS3Client
import java.io.ByteArrayInputStream
import com.amazonaws.services.s3.model.CannedAccessControlList
import org.apache.commons.io.IOUtils
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.model.StorageClass
import org.springframework.beans.factory.annotation.Value

trait StorageProvider {

    var cdnUrl: String

    var bucketName: String

    @Value("${s3.secretKey}") var secretKey: String = _
    @Value("${s3.accessKey}") var accessKey: String = _

    def getCdnUrl = cdnUrl

    def get(key: String): InputStream = {
        try {
            getAmazonS3Client.getObject(bucketName, key).getObjectContent
        } catch {
            case _ => null
        }
    }

    def getObjectUrl(filename: String): String = cdnUrl + "/" + filename

    def put(key: String, in: String) {
        put(key, in, "text/plain")
    }

    def put(key: String, in: String, contentType: String) {
        val array = in.getBytes
        val md = new ObjectMetadata()
        md.setContentLength(array.length)
        md.setContentType(contentType)

        val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md)
        request.setCannedAcl(CannedAccessControlList.PublicRead)
        request.setStorageClass(StorageClass.ReducedRedundancy)
        getAmazonS3Client.putObject(request)
        key
    }

    def put(key: String, in: InputStream) {

        val bytes = IOUtils.toByteArray(in)
        val array = bytes.toArray
        val md = new ObjectMetadata()
        md.setContentLength(array.length)

        val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md);
        request.setCannedAcl(CannedAccessControlList.PublicRead)
        getAmazonS3Client.putObject(request)
        key
    }

    def getAmazonS3Client: AmazonS3Client = {
        val cred = new BasicAWSCredentials(accessKey, secretKey)
        new AmazonS3Client(cred)
    }
}