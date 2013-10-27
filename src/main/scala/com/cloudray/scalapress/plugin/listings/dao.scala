package com.cloudray.scalapress.plugin.listings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.cloudray.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage, ListingProcess}

/** @author Stephen Samuel */
trait ListingPackageDao extends GenericDao[ListingPackage, java.lang.Long] {
  def enabled: Boolean
}

@Component
@Transactional
class ListingPackageDaoImpl extends GenericDaoImpl[ListingPackage, java.lang.Long] with ListingPackageDao {
  def enabled: Boolean = findAll.size > 0
}

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