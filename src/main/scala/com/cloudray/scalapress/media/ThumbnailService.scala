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

  val THUMBNAIL_PREFIX = "_thumbnails"
  val MAX_BYTES = 1024 * 1024 * 10

  @Autowired var assetStore: AssetStore = _

  val manager = CacheManager.getInstance()

  val config = new CacheConfiguration()
  config.setMaxBytesLocalHeap(MAX_BYTES)
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

  def _setCached(key: String): Unit = cache.putIfAbsent(new Element(key, true))

  /**
   * Returns a link to a thumbnail generated version of the given image.
   * If the thumbnail is known to exist then the link will be to the asset store version.
   * If the thumbnail does not exist then it will point to the thumbnail controller to one
   * can be generated and persisted.
   */
  def link(filename: String, w: Int, h: Int, opType: OpType): String = {
    val key = _thumbkey(filename, w, h, opType)
    if (cache.get(key) != null) assetStore.link(key)
    else s"/thumbnail/$filename?w=$w&h=$h&opType=$opType"
  }

  def thumbnail(filename: String, w: Int, h: Int, t: OpType): Option[Scrimage] = {
    val key = _thumbkey(filename, w, h, t)
    _fetch(key) match {
      case Some(thumb) =>
        _setCached(key)
        Some(thumb)
      case _ =>
        val thumb = _generate(filename, w, h, t)
        thumb.map(_store(key, _))
        _setCached(key)
        thumb
    }
  }

  def _fetch(key: String): Option[Scrimage] = assetStore.get(key).map(in => Scrimage(in))

  // returns the key of what the asset should be stored under at the asset store
  def _thumbkey(filename: String, w: Int, h: Int, opType: OpType): String = {
    val basename = FilenameUtils.getBaseName(filename)
    s"$THUMBNAIL_PREFIX/${basename}_${opType}_${w}x$h.png"
  }

  def _store(filename: String, image: Scrimage): Unit = {
    logger.debug("Persisting thumbnail [{}]", filename)
    assetStore.put(filename, new ByteArrayInputStream(image.write(Format.PNG)))
  }

  def _generate(filename: String, w: Int, h: Int, opType: OpType): Option[Scrimage] = {
    logger.debug("Generating thumbnail [{}]", Array(filename, w, h, opType))
    _fetch(filename).map(_resize(_, w, h, opType))
  }

  def _resize(image: Scrimage, w: Int, h: Int, opType: OpType): Scrimage = opType match {
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