package com.liferay.scalapress.settings

import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.Redirect
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.GenericDao

/** @author Stephen Samuel */
trait RedirectDao extends GenericDao[Redirect, java.lang.Long]

@Component
@Transactional
class RedirectDaoImpl extends GenericDaoImpl[Redirect, java.lang.Long] with RedirectDao
