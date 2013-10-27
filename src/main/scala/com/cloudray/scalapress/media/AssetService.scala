package com.cloudray.scalapress.media

import java.io.InputStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
@Component
@Autowired
class AssetService(assetStore: AssetStore,
                   context: ScalapressContext) {

  /** Removes the given key from the asset store, or does nothing if the key does not exist
    */
  def delete(key: String): Unit = assetStore.delete(key)

  /** Adds the given multipart files to the asset store using the original
    * filenames as the keys. If a key is already in use then it will modify
    * the key so that it is unique.
    *
    * This method will silently ignore any files that have zero byte content.
    *
    * @return the keys that the assets were stored under
    */
  def upload(files: Seq[MultipartFile]): Seq[String] = {
    for ( file <- files if !file.isEmpty ) yield upload(file)
  }

  /** Adds the given multipart file to the asset store using the original
    * filename as the key. If the key is already in use then it will modify
    * the key so that it is unique.
    *
    * @return the key that the asset was stored under
    */
  def upload(file: MultipartFile): String = {
    val key = file.getOriginalFilename
    val in = file.getInputStream
    add(key, in)
  }

  /** Adds the given stream under a unique key. The given key is used as a hint and may
    * not be the actual key used. This method guarantees not to overwrite any existing asset.
    *
    * @return the key that the asset was stored under
    */
  def add(key: String, input: InputStream): String = {
    val asset = adapt(key, input)
    assetStore.add(asset._1, asset._2)
  }

  /** Adds the given stream to the asset store, overriding any existing asset that
    * is stored under the same key. This method guarantees to use the key provided.
    */
  def put(key: String, input: InputStream) {
    val asset = adapt(key, input)
    assetStore.put(asset._1, asset._2)
  }

  private def adapt(asset: (String, InputStream)): (String, InputStream) = {
    val listeners = context.beans[AssetLifecycleListener]
    val op = (a: (String, InputStream), b: AssetLifecycleListener) => b.onStore(a._1, a._2)
    listeners.foldLeft(asset)(op)
  }
}
