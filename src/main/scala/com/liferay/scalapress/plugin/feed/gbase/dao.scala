package com.liferay.scalapress.plugin.feed.gbase

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait GBaseFeedDao extends GenericDao[GBaseFeed, java.lang.Long]

@Component
@Transactional
class GBaseFeedDaoImpl extends GenericDaoImpl[GBaseFeed, java.lang.Long] with GBaseFeedDao