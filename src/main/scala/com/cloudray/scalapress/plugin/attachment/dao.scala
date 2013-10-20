package com.cloudray.scalapress.plugin.attachment

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait AttachmentDao extends GenericDao[Attachment, java.lang.Long]

@Component
@Transactional
class AttachmentDaoImpl extends GenericDaoImpl[Attachment, java.lang.Long] with AttachmentDao
