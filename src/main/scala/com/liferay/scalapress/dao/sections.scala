package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.section.Section
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */

trait PluginDao extends GenericDao[Section, java.lang.Long]

@Component
@Transactional
class PluginDaoImpl extends GenericDaoImpl[Section, java.lang.Long] with PluginDao