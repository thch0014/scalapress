package com.cloudray.scalapress.search.widget

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.search.{SearchFormRenderer, SearchForm}
import scala.beans.BeanProperty
import javax.persistence._

/** @author Stephen Samuel
  *
  *         Shows a search box with options
  *
  * */
@Entity
@Table(name = "boxes_search")
class SearchFormWidget extends Widget {

  @OneToOne
  @JoinColumn(name = "searchForm")
  @BeanProperty var searchForm: SearchForm = _

  @Column(length = 1000)
  var noResultsText: String = _

  override def _init(context: ScalapressContext) {
    searchForm = new SearchForm
    context.searchFormDao.save(searchForm)
  }

  def render(request: ScalapressRequest): Option[String] = Option(searchForm) match {
    case Some(form) =>
      val rendered = SearchFormRenderer.render(form, this)
      Some(rendered)
    case None => Some("<!--no search form set-->")
  }

  override def backoffice = "/backoffice/plugin/search/widget/searchform/" + id
}
