package com.cloudray.scalapress.plugin.search.tags

import com.cloudray.scalapress.widgets.Widget
import javax.persistence.{Entity, Table}
import com.cloudray.scalapress.search.{SearchService, SavedSearch}
import scala.xml.Utility
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Table(name = "plugin_tagswidget")
@Entity
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
