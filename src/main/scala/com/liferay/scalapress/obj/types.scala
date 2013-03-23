package com.liferay.scalapress.obj

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait TypeDao extends GenericDao[ObjectType, java.lang.Long] {
    def getAccount: Option[ObjectType]
}

@Component
@Transactional
class TypeDaoImpl extends GenericDaoImpl[ObjectType, java.lang.Long] with TypeDao {
    def getAccount: Option[ObjectType] =
        findAll.find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts")
}
