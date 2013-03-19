package com.liferay.scalapress.plugin.ecommerce.dao

import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait DeliveryOptionDao extends GenericDao[DeliveryOption, java.lang.Long]

@Component
@Transactional
class DeliveryOptionDaoImpl extends GenericDaoImpl[DeliveryOption, java.lang.Long] with DeliveryOptionDao