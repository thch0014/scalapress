package com.liferay.scalapress.plugin.attachment

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.media.AssetStore
import org.mockito.{Mockito, Matchers}

/** @author Stephen Samuel */
class AttachmentSectionTest extends FunSuite with MockitoSugar {

    val attachment = new Attachment
    attachment.name = "coldplay live video"
    attachment.filename = "coldplayz.mp4"

    val assetStore = mock[AssetStore]

    test("render attachment row happy path") {
        Mockito.when(assetStore.link(Matchers.anyString)).thenReturn("http://mock.domain.com/file.pdf")
        val attachments = List(attachment)
        val rendered = new AttachmentSection()._renderAttachments(attachments, assetStore)
        assert(
            List(<div class="attachment-row"><div class="attachment-name">coldplay live video</div><div class="attachment-name"></div><div class="attachment-link"><a href="http://mock.domain.com/file.pdf">Download file</a></div></div>) === rendered)
    }
}
