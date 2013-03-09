package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.form.{Form, Submission}
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait FormDao extends GenericDao[Form, java.lang.Long]

@Component
@Transactional
class FormDaoImpl extends GenericDaoImpl[Form, java.lang.Long] with FormDao

trait SubmissionDao extends GenericDao[Submission, java.lang.Long]

@Component
@Transactional
class SubmissionDaoImpl extends GenericDaoImpl[Submission, java.lang.Long] with SubmissionDao
