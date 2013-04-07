package com.liferay.scalapress.folder

import javax.persistence.{EnumType, Enumerated, ManyToOne, JoinColumn, FetchType, Column, Table, Entity}
import scala.collection.JavaConverters._
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import com.liferay.scalapress.enums.Sort
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.{MarkupRenderer, Markup}

/** @author Stephen Samuel
  *
  *         Shows a list of objects inside a folder.
  *
  * */
@Entity
@Table(name = "blocks_items")
class ObjectListSection extends Section {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sortType")
    @BeanProperty var sort: Sort = _

    @Column(name = "itemsPerPage")
    @BeanProperty var pageSize: Int = _

    @Column(name = "includeSubcategoryItems")
    @BeanProperty var includeSubfolderObjects: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listMarkup", nullable = true)
    @BeanProperty var markup: Markup = _

    override def backoffice: String = "/backoffice/section/objectlist/" + id

    def desc = "Show a paginated list of objects that are inside this folder"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val objects = try {
            folder.objects.asScala.toSeq
        } catch {
            case e: Exception => Nil
        }
        val live = objects.filter(_.status.toLowerCase == "live")

        val sorted = sort match {
            case Sort.Price => live.sortBy(_.sellPrice)
            case Sort.PriceHigh => live.sortBy(_.sellPrice).reverse
            case Sort.Newest => live.sortBy(_.id).reverse
            case Sort.Oldest => live.sortBy(_.id)
            case _ => live.sortBy(_.name)
        }

        sorted.size match {
            case 0 => Some("<!-- No objects in folder -->")
            case _ => {
                val first = sorted.head
                Option(markup).orElse(Option(first.objectType.objectListMarkup)) match {
                    case None => Some("<!-- No markup found for folder -->")
                    case Some(m) => {
                        Some(MarkupRenderer.renderObjects(sorted, m, request, context))
                    }
                }
            }
        }
    }
}
