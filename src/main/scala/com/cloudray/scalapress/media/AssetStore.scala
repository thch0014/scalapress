package com.cloudray.scalapress.media

import java.io.InputStream
import scala.deprecated

/** @author Stephen Samuel */
trait AssetStore {

  /** Returns the number of assets in the asset store.
    * Implementations are permitted to use best effort when counting.
    * This is useful for backing stores that adhere to eventual consistency where
    * exact counts would be costly.
    */
  def count: Int

  /** Returns true if the given key exists in the store.
    */
  def exists(key: String): Boolean

  // adds the given stream with a generated id and returns that id
  @deprecated("should be provided with a key, let consumer of this api generate one if needed", "0.39")
  def add(input: InputStream): String

  /** Adds the given stream under a unique key. The given key is used as a hint and may
    * not be the actual key used. This method guarantees not to overwrite any existing asset.
    *
    * @return the key that the asset was stored under
    */
  def add(key: String, input: InputStream): String

  /** Adds the given stream to the asset store, overriding any existing asset that
    * is stored under the same key. This method guarantees to use the key provided.
    */
  def put(key: String, input: InputStream)

  /** returns a stream of the given key or none if the file was not found
    */
  def get(key: String): Option[InputStream]

  /** Removes the given key from the asset store, or does nothing if the key does not exist
    */
  def delete(key: String)

  /** Searches the index for assets that match the given query.
    * Implementations are permitted to use best effort when performing the search.
    * This is useful for backing stores that adhere to eventual consistency where
    * exact searches would be costly.
    */
  def search(query: AssetQuery): Array[Asset]

  /** Returns an externally accessible base URL for this asset store.
    */
  @deprecated("not all images might have the same base url, find a way around this for future", "0.39")
  def baseUrl: String

  /** Returns an externally accessible base URL for the given asset key
    */
  def link(key: String): String

}

case class AssetQuery(prefix: Option[String], pageNumber: Int, pageSize: Int) {
  def offset: Int = (pageNumber - 1) * pageSize
}
object AssetQuery {
  def apply(pageNumber: Int, pageSize: Int): AssetQuery = apply(None, pageNumber, pageSize)
  def apply(prefix: String, pageNumber: Int, pageSize: Int): AssetQuery =
    AssetQuery(Option(prefix), pageNumber, pageSize)
}
case class Asset(filename: String, size: Long, url: String, contentType: String)
