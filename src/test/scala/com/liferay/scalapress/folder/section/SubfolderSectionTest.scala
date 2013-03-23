package com.liferay.scalapress.folder.section

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class SubfolderSectionTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    test("that hidden folders are not included") {

        val folder2 = new Folder
        folder2.id = 2
        folder2.name = "visible"

        val folder3 = new Folder
        folder3.id = 3
        folder3.name = "hidden"
        folder3.hidden = true

        val root = new Folder
        root.id = 1
        root.subfolders.add(folder2)
        root.subfolders.add(folder3)

        val section = new SubfolderSection()
        section.folder = root
        val subfolders = section._folders
        assert(subfolders.size === 1)
        assert(subfolders(0) === folder2)
    }
}
