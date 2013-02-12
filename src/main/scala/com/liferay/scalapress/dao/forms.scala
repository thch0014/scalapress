package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.form.{Submission, Form}
import com.liferay.scalapress.{Page, PagedQuery}
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
trait FormDao extends GenericDao[Form, java.lang.Long]

@Component
@Transactional
class FormDaoImpl extends GenericDaoImpl[Form, java.lang.Long] with FormDao

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
