package com.liferay.scalapress.dao.settings

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.setup.{GeneralSettings, Installation}
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */

trait GeneralSettingsDao extends GenericDao[GeneralSettings, java.lang.Long]

@Component
@Transactional
class GeneralSettingsDaoImpl extends GenericDaoImpl[GeneralSettings, java.lang.Long] with GeneralSettingsDao

trait InstallationDao extends GenericDao[Installation, java.lang.Long] {
    def get: Installation
}

@Component
@Transactional
class InstallationDaoImpl extends GenericDaoImpl[Installation, java.lang.Long] with InstallationDao {

    var cached: Option[Installation] = None

    override def save(installation: Installation): Boolean = {
        val b = super.save(installation)
        cached = None
        b
    }

    override def get: Installation = {
        cached match {
            case Some(installation) => installation
            case None =>
                cached = Option(findAll.head)
                cached.get
        }
    }
}

@Component
class InstallationDaoImplValidator {
    @Autowired var dao: InstallationDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new Installation
            dao.save(plugin)
        }
    }
}
