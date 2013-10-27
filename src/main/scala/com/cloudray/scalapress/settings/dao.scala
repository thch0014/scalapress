package com.cloudray.scalapress.settings

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait GeneralSettingsDao extends GenericDao[GeneralSettings, java.lang.Long] {
  def get: GeneralSettings
}

@Component
@Transactional
class GeneralSettingsDaoImpl extends GenericDaoImpl[GeneralSettings, java.lang.Long] with GeneralSettingsDao {
  override def get: GeneralSettings = findAll.head
}

trait InstallationDao extends GenericDao[Installation, java.lang.Long] {
  def get: Installation
}

@Component
@Transactional
class InstallationDaoImpl extends GenericDaoImpl[Installation, java.lang.Long] with InstallationDao {
  override def get = findAll.head
}