package com.liferay.scalapress.plugin.search.tags

import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{Cacheable, Entity, Table}
import com.liferay.scalapress.search.{SearchService, SavedSearch}
import scala.xml.Utility
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Table(name = "plugin_tagswidget")
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class TagsWidget extends Widget {

    def render(req: ScalapressRequest): Option[String] = {

        val search = new SavedSearch
        search.facets = Seq(SearchService.FACET_TAGS)

        val result = req.context.searchService.search(search)
        val tags = result.facets.find(_.name == SearchService.FACET_TAGS) match {
            case None => Nil
            case Some(facet) => facet.terms.map(_.term)
        }

        val xml = <ul class="tags-widget">
            {_renderTags(tags)}
        </ul>
        Some(xml.toString())
    }

    def _renderTags(tags: Iterable[String]) =
        tags.map(tag => <span>
            {tag}
        </span>).map(Utility.trim(_))
}
