package com.liferay.scalapress.service.asset

import java.io.{ByteArrayInputStream, InputStream}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.util.UUID
import org.apache.commons.io.IOUtils
import com.amazonaws.services.s3.model.{S3ObjectSummary, ListObjectsRequest, StorageClass, CannedAccessControlList, PutObjectRequest, ObjectMetadata}
import java.util
import scala.collection.JavaConverters._
import java.net.URLConnection

/** @author Stephen Samuel */
class AmazonS3AssetStore(val cdnUrl: String,
                         val secretKey: String,
                         val accessKey: String,
                         val bucketName: String) extends AssetStore {

    def delete(key: String) {
        getAmazonS3Client.deleteObject(bucketName, key)
    }

    def search(query: String, limit: Int): Array[Asset] = {
        listObjects(query, 0, limit).toList.map(arg => Asset(arg.getKey,
            arg.getSize,
            link(arg.getKey),
            URLConnection.guessContentTypeFromName(arg.getKey)))
          .toArray
    }

    def cdn = cdnUrl

    def count: Int = {
        val req: ListObjectsRequest = new ListObjectsRequest
        req.setBucketName(bucketName)

        var listing = getAmazonS3Client.listObjects(req)
        var count = 0
        while (listing.getObjectSummaries.size > 0) {
            count = count + listing.getObjectSummaries.size()
            listing = getAmazonS3Client.listNextBatchOfObjects(listing)
        }
        count
    }

    /**
     * Lists all objects in the images bucket
     */
    private def listObjects(prefix: String, start: Int, limit: Int): Array[S3ObjectSummary] = {

        val req: ListObjectsRequest = new ListObjectsRequest
        req.setBucketName(bucketName)
        req.setPrefix(prefix)

        var listing = getAmazonS3Client.listObjects(req)
        var summaries = listing.getObjectSummaries
        val all = new util.ArrayList[S3ObjectSummary](summaries)
        while (summaries.size > 0) {
            listing = getAmazonS3Client.listNextBatchOfObjects(listing)
            summaries = listing.getObjectSummaries
            all.addAll(summaries)
        }
        all.asScala.toArray.drop(start).take(limit)
    }

    override def link(key: String) = cdnUrl + "/" + key

    override def list(limit: Int): Array[Asset] = search(null, limit)

    def get(key: String): Option[InputStream] =
        Option(getAmazonS3Client.getObject(bucketName, key)).map(_.getObjectContent)

    def add(key: String, in: InputStream): String = {
        val normalizedKey = getNormalizedKey(key)
        put(normalizedKey, in)
    }

    def put(key: String, in: InputStream): String = {

        val array: Array[Byte] = IOUtils.toByteArray(in)

        val md = new ObjectMetadata
        md.setContentLength(array.length)
        md.setContentType(URLConnection.guessContentTypeFromStream(in))

        val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md)
        request.setCannedAcl(CannedAccessControlList.PublicRead)
        request.setStorageClass(StorageClass.ReducedRedundancy)

        getAmazonS3Client.putObject(request)
        key
    }

    private def getNormalizedKey(key: String): String = {
        try {
            getAmazonS3Client.getObject(bucketName, key)
            getNormalizedKey("copy_" + key)
        }
        catch {
            case e: com.amazonaws.AmazonClientException => key
        }
    }

    def add(in: InputStream): String = put(UUID.randomUUID.toString, in)

    def getAmazonS3Client: AmazonS3Client = {
        val cred: BasicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey)
        new AmazonS3Client(cred)
    }
}
