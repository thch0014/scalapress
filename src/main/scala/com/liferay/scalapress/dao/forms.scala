package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.form.{Submission, Form}

/** @author Stephen Samuel */
trait FormDao extends GenericDao[Form, java.lang.Long]

@Component
@Transactional
class FormDaoImpl extends GenericDaoImpl[Form, java.lang.Long] with FormDao

trait SubmissionDao extends GenericDao[Submission, java.lang.Long]

@Component
@Transactional
class SubmissionDaoImpl extends GenericDaoImpl[Submission, java.lang.Long] with SubmissionDao
