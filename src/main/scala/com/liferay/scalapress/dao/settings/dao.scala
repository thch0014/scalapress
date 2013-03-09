package com.liferay.scalapress.dao.settings

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.setup.{GeneralSettings, Site}
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */

trait GeneralSettingsDao extends GenericDao[GeneralSettings, java.lang.Long]

@Component
@Transactional
class GeneralSettingsDaoImpl extends GenericDaoImpl[GeneralSettings, java.lang.Long] with GeneralSettingsDao

trait SiteDao extends GenericDao[Site, java.lang.Long]

@Component
@Transactional
class SiteDaoImpl extends GenericDaoImpl[Site, java.lang.Long] with SiteDao