package com.liferay.scalapress.folder

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class FolderTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    test("given a folder then the full name includes the parents") {

        val f1 = new Folder
        f1.id = 2
        f1.name = "grandad"

        val f2 = new Folder
        f2.id = 2
        f2.name = "dad"
        f2.parent = f1

        val f3 = new Folder
        f3.id = 2
        f3.name = "son"
        f3.parent = f2

        val n = f3.fullName
        assert("grandad > dad > son" === n)
    }
}
