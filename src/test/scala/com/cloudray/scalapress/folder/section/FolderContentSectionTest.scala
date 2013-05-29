package com.cloudray.scalapress.folder.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class FolderContentSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val section = new FolderContentSection

    test("section backoffice url is absolute") {
        assert(section.backoffice.startsWith("/backoffice/"))
    }
}
