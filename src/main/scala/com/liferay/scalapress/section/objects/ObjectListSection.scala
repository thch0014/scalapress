package com.liferay.scalapress.section.objects

import com.liferay.scalapress.domain.{Markup, SortType}
import javax.persistence.{ManyToOne, JoinColumn, FetchType, Column, Table, Entity}
import scala.collection.JavaConverters._
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_items")
class ObjectListSection extends Section {

    @Column(name = "sortType")
    var sortType: SortType = _

    @Column(name = "itemsPerPage")
    var pageSize: Int = _

    @Column(name = "includeSubcategoryItems")
    var includeSubfolderObjects: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listMarkup", nullable = true)
    var markup: Markup = _

    @Column(name = "noItemsMessage")
    var emptyFolderMessage: String = _

    def desc = "Show a paginated list of objects that are inside this general"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        folder.objects.size match {
            case 0 => Option(emptyFolderMessage).orElse(Some("<!-- No empty message for folder -->"))
            case _ => {
                val first = folder.objects.asScala.head
                Option(markup).orElse(Option(first.objectType.listItemMarkup)) match {
                    case None => Some("<!-- No markup found for folder -->")
                    case Some(m) => {
                        Some(MarkupRenderer.renderObjects(folder.objects.asScala.toList, m, request, context))
                    }
                }
            }
        }
    }
}
