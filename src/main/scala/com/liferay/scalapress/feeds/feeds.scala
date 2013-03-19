package com.liferay.scalapress.feeds

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait FeedDao extends GenericDao[Feed, java.lang.Long]

@Component
@Transactional
class FeedDaoImpl extends GenericDaoImpl[Feed, java.lang.Long] with FeedDao