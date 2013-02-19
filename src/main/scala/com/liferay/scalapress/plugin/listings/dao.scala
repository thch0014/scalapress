package com.liferay.scalapress.plugin.listings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.plugin.ecommerce.domain.Basket
import com.liferay.scalapress.plugin.ecommerce.dao.BasketDao

/** @author Stephen Samuel */
trait ListingPackageDao extends GenericDao[ListingPackage, java.lang.Long]

@Component
@Transactional
class ListingPackageDaoImpl extends GenericDaoImpl[ListingPackage, java.lang.Long] with ListingPackageDao

trait ListingProcessDao extends GenericDao[ListingProcess, String]

@Component
@Transactional
class ListingProcessDaoImpl extends GenericDaoImpl[ListingProcess, String] with ListingProcessDao