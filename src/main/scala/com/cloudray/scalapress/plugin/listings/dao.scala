package com.cloudray.scalapress.plugin.listings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.cloudray.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage, ListingProcess}
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
trait ListingPackageDao extends GenericDao[ListingPackage, java.lang.Long]

@Component
@Transactional
class ListingPackageDaoImpl extends GenericDaoImpl[ListingPackage, java.lang.Long] with ListingPackageDao

trait ListingProcessDao extends GenericDao[ListingProcess, String]

@Component
@Transactional
class ListingProcessDaoImpl extends GenericDaoImpl[ListingProcess, String] with ListingProcessDao

trait ListingsPluginDao extends GenericDao[ListingsPlugin, java.lang.Long] {
    def get: ListingsPlugin
}

@Component
@Transactional
class ListingsPluginDaoImpl extends GenericDaoImpl[ListingsPlugin, java.lang.Long] with ListingsPluginDao {
    def get = findAll.head
}

@Component
class ListingsPluginDaoValidator {
    @Autowired var dao: ListingsPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new ListingsPlugin
            dao.save(plugin)
        }
    }
}