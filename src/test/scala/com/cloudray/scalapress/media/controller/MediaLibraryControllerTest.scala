package com.cloudray.scalapress.media.controller

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.admin.MediaLibraryController
import com.cloudray.scalapress.media.{AssetService, AssetStore, AssetLifecycleListener}
import java.io.{ByteArrayInputStream, File, InputStream}
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class MediaLibraryControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val lifecycle = new AssetLifecycleListener {
    def onStore(key: String, input: InputStream): (String, InputStream) = ("newkey.png", input)
  }
  val context = mock[ScalapressContext]
  val assetStore = mock[AssetStore]
  val assetService = mock[AssetService]
  val controller = new MediaLibraryController(assetStore, assetService, context)

  "a media library controller" should "apply asset lifecycles before storing" in {
    Mockito.when(context.beans[AssetLifecycleListener]).thenReturn(Iterable(lifecycle))
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
    val list = List[MultipartFile](upload, upload)
    controller.upload(list.asJava)
    Mockito.verify(assetService).upload(list)
  }
}
