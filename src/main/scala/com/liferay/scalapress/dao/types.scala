package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.ObjectType
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait TypeDao extends GenericDao[ObjectType, java.lang.Long]

@Component
@Transactional
class TypeDaoImpl extends GenericDaoImpl[ObjectType, java.lang.Long] with TypeDao
