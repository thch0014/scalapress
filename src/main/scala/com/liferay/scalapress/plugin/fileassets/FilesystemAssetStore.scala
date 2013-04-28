package com.liferay.scalapress.plugin.fileassets

import com.liferay.scalapress.media.{Asset, AssetStore}
import java.io.InputStream
import java.util.UUID

/** @author Stephen Samuel */
class FilesystemAssetStore extends AssetStore {

    // returns the number of items in this asset store
    def count: Int = 0

    // returns an externally accessible URL for the given asset
    def link(key: String): String = ""

    // returns an externally accessible base URL for this store.
    def baseUrl: String = "http://domain.com"

    def search(query: String, limit: Int): Array[Asset] = Array()

    def list(limit: Int): Array[Asset] = Array()

    def delete(key: String) {}

    def get(key: String): Option[InputStream] = None

    // adds the given stream and overrides any existing
    def put(key: String, input: InputStream) {}

    // adds the given stream with the given key, generating a new key if the provided one is in use
    def add(key: String, input: InputStream): String = key

    // adds the given stream with a generated id and returns that id
    def add(input: InputStream): String = UUID.randomUUID().toString

    def exists(key: String): Boolean = false
}
