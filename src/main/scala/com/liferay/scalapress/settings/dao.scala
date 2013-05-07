package com.liferay.scalapress.settings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

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
    override def get = findAll.head
}

@Component
class InstallationDaoImplValidator {
    @Autowired var dao: InstallationDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val i = new Installation
            dao.save(i)
        }
    }
}
