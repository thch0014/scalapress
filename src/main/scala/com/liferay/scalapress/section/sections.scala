package com.liferay.scalapress.section

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */

trait SectionDao extends GenericDao[Section, java.lang.Long]

@Component
@Transactional
class SectionDaoImpl extends GenericDaoImpl[Section, java.lang.Long] with SectionDao