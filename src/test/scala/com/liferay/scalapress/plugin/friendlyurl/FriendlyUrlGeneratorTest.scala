package com.liferay.scalapress.plugin.friendlyurl

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class FriendlyUrlGeneratorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

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
