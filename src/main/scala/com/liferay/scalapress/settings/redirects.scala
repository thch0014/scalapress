package com.liferay.scalapress.settings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.GenericDao
import com.liferay.scalapress.settings.Redirect

/** @author Stephen Samuel */
trait RedirectDao extends GenericDao[Redirect, java.lang.Long]

@Component
@Transactional
class RedirectDaoImpl extends GenericDaoImpl[Redirect, java.lang.Long] with RedirectDao
