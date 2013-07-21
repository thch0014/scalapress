package com.cloudray.scalapress.media.controller

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.admin.MediaLibraryController
import com.cloudray.scalapress.media.{AssetStore, AssetLifecycleListener}
import java.io.{ByteArrayInputStream, File, InputStream}
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import scala.collection.JavaConverters._
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
class MediaLibraryControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val lifecycle = new AssetLifecycleListener {
    def onStore(key: String, input: InputStream): (String, InputStream) = ("newkey.png", input)
  }
  val controller = new MediaLibraryController
  controller.context = mock[ScalapressContext]
  controller.assetStore = mock[AssetStore]

  "a media library controller" should "apply asset lifecycles before storing" in {
    Mockito.when(controller.context.beans[AssetLifecycleListener]).thenReturn(Iterable(lifecycle))
    val upload = new MultipartFile {
      val stream = new ByteArrayInputStream(getBytes)
      def getOriginalFilename: String = "filename.png"
      def getName: String = "filename.png"
      def getSize: Long = 2
      def transferTo(dest: File) {}
      def isEmpty: Boolean = false
      def getContentType: String = "image/png"
      def getInputStream: InputStream = stream
      def getBytes: Array[Byte] = Array[Byte](1, 2, 3)
    }
    controller.upload(List[MultipartFile](upload).asJava)
    Mockito.verify(controller.assetStore).put("newkey.png", upload.getInputStream)
  }
}
