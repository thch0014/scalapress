package com.liferay.scalapress

import folder.Folder
import obj.Obj
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class FriendlyUrlGeneratorTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    test("object friendly url happy path") {
        val obj = new Obj
        obj.id = 1234
        obj.name = "boro for the champo"
        assert("/object-1234-boro-for-the-champo" === FriendlyUrlGenerator.friendlyUrl(obj))
    }

    test("folder friendly url happy path") {
        val f = new Folder
        f.id = 55
        f.name = "uefa cup final 2006"
        assert("/folder-55-uefa-cup-final-2006" === FriendlyUrlGenerator.friendlyUrl(f))
    }
}
