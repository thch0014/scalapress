package com.cloudray.scalapress.plugin.attachment

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.media.AssetStore
import org.mockito.{Mockito, Matchers}

/** @author Stephen Samuel */
class AttachmentSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val attachment = new Attachment
    attachment.name = "coldplay live video"
    attachment.filename = "coldplayz.mp4"

    val assetStore = mock[AssetStore]

    test("render attachment row happy path") {
        Mockito.when(assetStore.link(Matchers.anyString)).thenReturn("http://mock.domain.com/file.pdf")
        val attachments = List(attachment)
        val rendered = new AttachmentSection()._renderAttachments(attachments, assetStore)
    }
}
