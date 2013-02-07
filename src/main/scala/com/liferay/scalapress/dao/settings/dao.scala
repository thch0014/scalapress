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

trait SiteDao extends GenericDao[Site, java.lang.Long] {
    def get: Site
}

@Component
@Transactional
class SiteDaoImpl extends GenericDaoImpl[Site, java.lang.Long] with SiteDao {

    var cached: Option[Site] = None

    override def save(site: Site): Boolean = {
        val b = super.save(site)
        cached = None
        b
    }

    override def get: Site = {
        cached match {
            case Some(site) => site
            case None =>
                cached = Option(findAll.head)
                cached.get
        }
    }
}