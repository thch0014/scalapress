package com.liferay.scalapress.service.asset

import java.io.InputStream

/** @author Stephen Samuel */
trait AssetStore {

    def exists(key: String): Boolean
    def add(input: InputStream): String
    def add(key: String, input: InputStream): String
    def put(key: String, input: InputStream): String
    def get(key: String): Option[InputStream]
    def delete(key: String)
    def list(limit: Int): Array[Asset]
    def search(query: String, limit: Int): Array[Asset]
    def link(key: String): String
    def cdn: String
    def count: Int
}

case class Asset(filename: String, size: Long, url: String, contentType: String)
