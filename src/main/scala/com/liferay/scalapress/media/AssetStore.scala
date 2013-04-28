package com.liferay.scalapress.media

import java.io.InputStream

/** @author Stephen Samuel */
trait AssetStore {

    def exists(key: String): Boolean

    // adds the given stream with a generated id and returns that id
    def add(input: InputStream): String

    // adds the given stream with the given key, generating a new key if the provided one is in use
    def add(key: String, input: InputStream): String

    // adds the given stream and overrides any existing
    def put(key: String, input: InputStream)

    // returns a stream of the given key or none if the file was not found
    def get(key: String): Option[InputStream]

    def delete(key: String)

    def list(limit: Int): Array[Asset]

    def search(query: String, limit: Int): Array[Asset]

    // returns an externally accessible base URL for this store.
    def baseUrl: String

    // returns an externally accessible URL for the given asset
    def link(key: String): String

    // returns the number of items in this asset store
    def count: Int
}

case class Asset(filename: String, size: Long, url: String, contentType: String)
