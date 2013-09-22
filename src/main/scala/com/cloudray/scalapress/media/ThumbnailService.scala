package com.cloudray.scalapress.media

import org.apache.commons.io.FilenameUtils
import com.sksamuel.scrimage.{Image => Scrimage, Format}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import com.cloudray.scalapress.Logging
import net.sf.ehcache.{CacheManager, Element, Cache}
import net.sf.ehcache.config.CacheConfiguration
import javax.annotation.PostConstruct
import java.util.UUID

@Component
class ThumbnailService extends Logging {

  val THUMBNAIL_PREFIX = "_thumbnail"

  @Autowired var assetStore: AssetStore = _

  val manager = CacheManager.getInstance()

  val config = new CacheConfiguration()
  config.setEternal(true)
  config.setMaxBytesLocalHeap("1m")
  config.setMaxElementsOnDisk(0)
  config.setName("thumbnail-cache-" + UUID.randomUUID())
  config.setMemoryStoreEvictionPolicy("LRU")

  val cache = new Cache(config)
  manager.addCacheIfAbsent(cache)

  @PostConstruct
  def populate() {
    for ( asset <- assetStore.list(THUMBNAIL_PREFIX, 10000) ) {
      _setCached(asset.filename)
    }
  }

  def _setCached(key: String): Unit = cache.put(new Element(key, true))

  /**
   * Returns a link to the asset store of a thumbnail generated version of the given image.
   * This method will ensure that the thumbnail version has been created and stored at the asset store.
   */
  def link(filename: String, w: Int, h: Int, opType: OpType): String = {
    val key = _thumbkey(filename, w, h, opType)
    if (!_exists(key))
      _thumbnail(filename, w, h, opType)
    assetStore.link(key)
  }

  def _exists(key: String): Boolean = {
    if (cache.get(key) != null) {
      true
    } else if (assetStore.exists(key)) {
      _setCached(key)
      true
    } else {
      false
    }
  }

  // returns the key of what the asset should be stored under at the asset store
  def _thumbkey(filename: String, w: Int, h: Int, opType: OpType): String = {
    val basename = FilenameUtils.getBaseName(filename)
    s"_thumbnails/${basename}_${opType}_${w}x$h.png"
  }

  // generate and store a thumbnail
  def _thumbnail(filename: String, w: Int, h: Int, t: OpType): Option[Scrimage] = {
    logger.debug("Generating thumbnail [{}]", Array(filename, w, h, t))
    val thumb = _generate(filename, w, h, t)
    val key = _thumbkey(filename, w, h, t)
    thumb.map(_store(key, _))
    thumb
  }

  def _store(filename: String, image: Scrimage): Unit = {
    logger.debug("Persisting thumbnail [{}]", filename)
    assetStore.put(filename, new ByteArrayInputStream(image.write(Format.PNG)))
  }

  // generate a thumbnail and returns it
  def _generate(filename: String, w: Int, h: Int, opType: OpType): Option[Scrimage] = {
    assetStore.get(filename) match {
      case Some(image) =>
        Option(_transform(Scrimage(image), w, h, opType))
      case _ =>
        logger.debug("Original image could not be found, unable to create thumbnail")
        None
    }
  }

  /**
   * performs the resize of the given image, using the method specified by type
   */
  def _transform(image: Scrimage, w: Int, h: Int, opType: OpType): Scrimage = opType match {
    case Bound => image.bound(w, h)
    case Cover => image.cover(w, h)
    case Fit => image.fit(w, h)
  }
}

trait OpType {
  def _opType(option: Option[String]) = option match {
    case Some("bound") => Bound
    case Some("cover") => Cover
    case _ => Fit
  }
}
case object Bound extends OpType {
  override def toString = "bound"
}
case object Cover extends OpType {
  override def toString = "cover"
}
case object Fit extends OpType {
  override def toString = "fit"
}