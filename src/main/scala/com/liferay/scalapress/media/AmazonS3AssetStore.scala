package com.liferay.scalapress.media

import java.io.{ByteArrayInputStream, InputStream}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.util.UUID
import org.apache.commons.io.{FilenameUtils, IOUtils}
import com.amazonaws.services.s3.model.{CopyObjectRequest, S3ObjectSummary, ListObjectsRequest, StorageClass, CannedAccessControlList, PutObjectRequest, ObjectMetadata}
import java.util
import scala.collection.JavaConverters._
import java.net.URLConnection
import com.liferay.scalapress.Logging
import actors.Futures

/** @author Stephen Samuel */
class AmazonS3AssetStore(val cdnUrl: String,
                         val secretKey: String,
                         val accessKey: String,
                         val bucketName: String) extends AssetStore with Logging {

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

    def exists(key: String) = {
        try {
            getAmazonS3Client.getObjectMetadata(bucketName, key) != null
        } catch {
            case e: Exception => false
        }
    }

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

    override def link(key: String) = "http://" + cdnUrl.replace("http://", "") + "/" + key

    override def list(limit: Int): Array[Asset] = search(null, limit)

    def get(key: String): Option[InputStream] = {
        try {
            Option(getAmazonS3Client.getObject(bucketName, key)) match {
                case None => None
                case Some(obj) =>
                    Option(obj.getObjectContent) match {
                        case Some(in) =>
                            val b = IOUtils.toByteArray(in)
                            if (b.length == 0)
                                None
                            else
                                Option(new ByteArrayInputStream(b))
                        case None => None
                    }
            }
        } catch {
            case e: Exception => None
        }
    }

    def add(key: String, in: InputStream): String = {
        val normalizedKey = getNormalizedKey(key)
        put(normalizedKey, in)
    }

    def put(key: String, in: InputStream): String = {

        val array: Array[Byte] = IOUtils.toByteArray(in)

        val md = new ObjectMetadata
        md.setContentLength(array.length)
        md.setContentType(ImageTools.contentType(key))

        val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md)
        request.setCannedAcl(CannedAccessControlList.PublicRead)
        request.setStorageClass(StorageClass.ReducedRedundancy)

        getAmazonS3Client.putObject(request)
        key
    }

    private def getNormalizedKey(key: String): String = {
        FilenameUtils.getBaseName(key) + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(key)
    }

    def add(in: InputStream): String = put(UUID.randomUUID.toString, in)

    def getAmazonS3Client: AmazonS3Client = {
        val cred: BasicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey)
        new AmazonS3Client(cred)
    }

    def run() {
        Futures.future {
            val list = listObjects(null, 0, 100000)
            logger.debug("Updating content type on {} objects", list.size)
            list.foreach(arg => {

                try {

                    val md = new ObjectMetadata
                    md.setContentType(ImageTools.contentType(arg.getKey))

                    val copy = new CopyObjectRequest(bucketName, arg.getKey, bucketName, arg.getKey)
                    copy.setNewObjectMetadata(md)
                    copy.setCannedAccessControlList(CannedAccessControlList.PublicRead)
                    copy.setStorageClass(StorageClass.Standard)
                    getAmazonS3Client.copyObject(copy)

                } catch {
                    case e: Exception => logger.warn("{}", e)
                }
            })
            logger.debug("Upgrade completed", list.size)
        }
    }
}
