package com.cloudray.scalapress.plugin.asset.amazons3

import java.io.{ByteArrayInputStream, InputStream}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import org.apache.commons.io.{FilenameUtils, IOUtils}
import com.amazonaws.services.s3.model._
import java.net.URLConnection
import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.media.{AssetQuery, MimeTools, AssetStore, Asset}
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
class AmazonS3AssetStore(val cdnUrl: String,
                         val secretKey: String,
                         val accessKey: String,
                         val bucketName: String) extends AssetStore with Logging {

  val CACHE_CONTROL = "max-age=2592000"
  val STORAGE_CLASS = StorageClass.ReducedRedundancy

  var assets: List[Asset] = Nil

  override def count: Int = assets.size

  override def delete(key: String): Unit = {
    getAmazonS3Client.deleteObject(bucketName, key)
    assets = assets.filterNot(_.filename == key)
  }

  override def exists(key: String) = {
    try {
      getAmazonS3Client.getObjectMetadata(bucketName, key) != null
    } catch {
      case e: Exception => false
    }
  }

  override def baseUrl = cdnUrl
  override def link(key: String) = "http://" + cdnUrl.replace("http://", "") + "/" + key

  override def get(key: String): Option[InputStream] = {
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

  override def search(q: AssetQuery): Array[Asset] = {
    val filtered = q.prefix match {
      case Some(prefix) => {
        val lowerPrefix = prefix.toLowerCase
        assets.filter(_.filename.toLowerCase.startsWith(lowerPrefix))
      }
      case None => assets
    }
    filtered.drop(q.offset).take(q.pageSize).toArray
  }

  override def add(key: String, in: InputStream): String = {
    val normalizedKey = getNormalizedKey(key)
    put(normalizedKey, in)
    normalizedKey
  }

  override def put(key: String, in: InputStream) {

    val array = IOUtils.toByteArray(in)
    IOUtils.closeQuietly(in)

    val md = new ObjectMetadata
    md.setContentLength(array.length)
    md.setContentType(MimeTools.contentType(key))
    md.setCacheControl(CACHE_CONTROL)

    val request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(array), md)
    request.setCannedAcl(CannedAccessControlList.PublicRead)
    request.setStorageClass(STORAGE_CLASS)

    getAmazonS3Client.putObject(request)
    assets = Asset(key, array.length, link(key), contentType(key)) :: assets
  }

  def getNormalizedKey(key: String): String = baseName(key) + "_" + System.currentTimeMillis() + "." + extension(key)
  def baseName(key: String): String = FilenameUtils.getBaseName(key)
  def extension(key: String): String = FilenameUtils.getExtension(key)
  def contentType(filename: String): String = URLConnection.guessContentTypeFromName(filename)

  def toAsset(arg: S3VersionSummary) = Asset(arg.getKey, arg.getSize, link(arg.getKey), contentType(arg.getKey))

  @PostConstruct
  def loadAssets() {

    val assets = new ListBuffer[Asset]

    val req = new ListVersionsRequest
    req.setBucketName(bucketName)

    var listing = getAmazonS3Client.listVersions(req)
    var versions = listing.getVersionSummaries
    while (versions != null && versions.size > 0) {
      assets appendAll versions.asScala.map(toAsset)
      listing = getAmazonS3Client.listNextBatchOfVersions(listing)
      versions = listing.getVersionSummaries
    }

    logger.info("Loaded assets [count: {}]", assets.size)
    this.assets = assets.toList
  }

  def getAmazonS3Client: AmazonS3Client = {
    val cred: BasicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey)
    new AmazonS3Client(cred)
  }
}
