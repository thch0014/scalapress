package com.cloudray.scalapress.search.widget

import javax.persistence._
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.search.SearchForm
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

/** @author Stephen Samuel
  *
  *         Shows a search box with options
  *
  * */
@Entity
@Table(name = "boxes_search")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class SearchFormWidget extends Widget {

    @OneToOne
    @JoinColumn(name = "searchForm")
    @BeanProperty var searchForm: SearchForm = _

    override def _init(context: ScalapressContext) {
        searchForm = new SearchForm
        context.searchFormDao.save(searchForm)
    }

    def render(req: ScalapressRequest): Option[String] = None

    override def backoffice = "/backoffice/plugin/search/widget/searchform/" + id
}
