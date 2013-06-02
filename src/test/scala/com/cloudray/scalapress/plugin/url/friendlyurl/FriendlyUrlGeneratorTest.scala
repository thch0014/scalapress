package com.cloudray.scalapress.plugin.url.friendlyurl

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
class FriendlyUrlGeneratorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("object friendly url happy path") {
        val obj = new Obj
        obj.id = 1234
        obj.name = "boro for the champo"
        assert("/object-1234-boro-for-the-champo" === UrlGenerator.url(obj))
    }

    test("folder friendly url happy path") {
        val f = new Folder
        f.id = 55
        f.name = "uefa cup final 2006"
        assert("/folder-55-uefa-cup-final-2006" === UrlGenerator.url(f))
    }
}