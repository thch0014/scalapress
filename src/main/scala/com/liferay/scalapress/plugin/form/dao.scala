package com.liferay.scalapress.plugin.form

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.{PagedQuery, Page}

/** @author Stephen Samuel */
trait FormDao extends GenericDao[Form, java.lang.Long]

@Component
@Transactional
class FormDaoImpl extends GenericDaoImpl[Form, java.lang.Long] with FormDao

trait FormFieldDao extends GenericDao[FormField, java.lang.Long]

@Component
@Transactional
class FormFieldDaoImpl extends GenericDaoImpl[FormField, java.lang.Long] with FormFieldDao

trait SubmissionDao extends GenericDao[Submission, java.lang.Long] {
    def search(q: PagedQuery): Page[Submission]
}

@Component
@Transactional
class SubmissionDaoImpl extends GenericDaoImpl[Submission, java.lang.Long] with SubmissionDao {
    def search(q: PagedQuery): Page[Submission] = {
        val s = new Search(classOf[Submission]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("id", true)
        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }
}
