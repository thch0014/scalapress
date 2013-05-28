package com.cloudray.scalapress.settings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait RedirectDao extends GenericDao[Redirect, java.lang.Long]

@Component
@Transactional
class RedirectDaoImpl extends GenericDaoImpl[Redirect, java.lang.Long] with RedirectDao
