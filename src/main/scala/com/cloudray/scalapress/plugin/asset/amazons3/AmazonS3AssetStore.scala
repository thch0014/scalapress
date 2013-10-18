package com.cloudray.scalapress.plugin.asset.amazons3

import java.io.{ByteArrayInputStream, InputStream}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.util.UUID
import org.apache.commons.io.{FilenameUtils, IOUtils}
import com.amazonaws.services.s3.model._
import java.net.URLConnection
import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.media.{MimeTools, AssetStore}
import org.joda.time.{DateTimeZone, DateTime}
import com.sksamuel.scoot.soa.PagedQuery
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.media.Asset
import scala.Some

/** @author Stephen Samuel */
class AmazonS3AssetStore(val cdnUrl: String,
                         val secretKey: String,
                         val accessKey: String,
                         val bucketName: String) extends AssetStore with Logging {

  val CACHE_CONTROL = "max-age=2592000"
  val STORAGE_CLASS = StorageClass.ReducedRedundancy

  def delete(key: String) {
    getAmazonS3Client.deleteObject(bucketName, key)
  }

  def search(query: String, pageNumber: Int, pageSize: Int): Array[Asset] = {
    val pq = PagedQuery(pageNumber, pageSize)
    listObjects(Option(query), pq.offset, pageSize).toList
      .map(arg => Asset(arg.getKey, arg.getSize, link(arg.getKey), URLConnection.guessContentTypeFromName(arg.getKey)))
      .toArray
  }

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
  private def listObjects(prefix: Option[String], start: Int, limit: Int): Seq[S3VersionSummary] = {

    val req = new ListVersionsRequest
    req.setBucketName(bucketName)
    req.setPrefix(prefix.orNull)
    req.setDelimiter("/")
    req.setMaxResults(1000)

    val buffer = new ListBuffer[S3VersionSummary]

    import scala.collection.JavaConverters._

    var listing = getAmazonS3Client.listVersions(req)
    var versions = listing.getVersionSummaries
    while (versions != null && versions.size > 0) {
      buffer.appendAll(versions.asScala)
      listing = getAmazonS3Client.listNextBatchOfVersions(listing)
      versions = listing.getVersionSummaries
    }
    buffer.drop(start).take(limit)
  }

  override def baseUrl = cdnUrl

  override def link(key: String) = "http://" + cdnUrl.replace("http://", "") + "/" + key

  override def list(limit: Int): Array[Asset] = search(null, 0, limit)

  override def list(prefix: String, limit: Int): Array[Asset] = search(prefix, 0, limit)

  def get(key: String): Option[InputStream] = {
    try {
      Option(getAmazonS3Client.getObject(bucketName, key)) match {
        case None => None
        case Some(obj) =>
          Option(obj.getObjectContent) match {
            case Some(in) =>
              val b = IOUtils.toByteArray(in)
              IOUtils.closeQuietly(in)
              if (b.length == 0) None
              else Option(new ByteArrayInputStream(b))
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
    normalizedKey
  }

  def put(key: String, in: InputStream) {

    val array: Array[Byte] = IOUtils.toByteArray(in)
    IOUtils.closeQuietly(in)

    val md = new ObjectMetadata
    md.setContentLength(array.length)
    md.setContentType(MimeTools.contentType(key))
    md.setCacheControl(CACHE_CONTROL)

    val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md)
    request.setCannedAcl(CannedAccessControlList.PublicRead)
    request.setStorageClass(STORAGE_CLASS)

    getAmazonS3Client.putObject(request)
  }

  def getNormalizedKey(key: String): String = {
    FilenameUtils.getBaseName(key) + "_" + new DateTime(DateTimeZone.UTC).getMillis + "." + FilenameUtils
      .getExtension(key)
  }

  def add(in: InputStream): String = {
    val key = UUID.randomUUID.toString
    put(key, in)
    key
  }

  def getAmazonS3Client: AmazonS3Client = {
    val cred: BasicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey)
    new AmazonS3Client(cred)
  }
}
