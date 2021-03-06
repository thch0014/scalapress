package com.cloudray.scalapress.media

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.asset.amazons3.AmazonS3AssetStore

/** @author Stephen Samuel */
class AmazonS3AssetStoreTest extends FunSuite with MockitoSugar {

  val store = new AmazonS3AssetStore("", "", "", "")

  test("normalized file happy path") {
    val normalized = store.getNormalizedKey("myfile.png")
    assert(normalized.matches("myfile_\\d{10,}.png"))
  }
}
