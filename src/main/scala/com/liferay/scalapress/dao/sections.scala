package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */

trait PluginDao extends GenericDao[Section, java.lang.Long]

@Component
@Transactional
class PluginDaoImpl extends GenericDaoImpl[Section, java.lang.Long] with PluginDao