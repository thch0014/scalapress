package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.plugin.ecommerce.dao.AddressDao
import com.cloudray.scalapress.plugin.ecommerce.domain.Address
import com.googlecode.genericdao.search.ISearch

/** @author Stephen Samuel */
class AddressPopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val a1 = new Address
    a1.id = 3
    a1.name = "Murdock"

    val a2 = new Address
    a2.id = 4
    a2.name = "Face man"

    val a3 = new Address
    a3.id = 2
    a3.name = "hannibal"

    val populator = new AddressPopulator {
        var addressDao: AddressDao = mock[AddressDao]
    }

    Mockito.when(populator.addressDao.search(Matchers.any[ISearch])).thenReturn(List(a1, a2, a3))

    test("that addresses are populated in id order") {
        val model = new ModelMap
        populator.addressOptions(4l, model)
        val addresses = model.get("addressesMap").asInstanceOf[java.util.Map[String, String]]
        assert(4 === addresses.size)
        val it = addresses.entrySet().iterator()
        assert("-Select Address-" === it.next().getValue)
        assert("hannibal null null null" === it.next().getValue)
        assert("Murdock null null null" === it.next().getValue)
        assert("Face man null null null" === it.next().getValue)
    }

}
