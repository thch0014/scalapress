package com.liferay.scalapress.feeds

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait FeedDao extends GenericDao[Feed, java.lang.Long]

@Component
@Transactional
class FeedDaoImpl extends GenericDaoImpl[Feed, java.lang.Long] with FeedDao