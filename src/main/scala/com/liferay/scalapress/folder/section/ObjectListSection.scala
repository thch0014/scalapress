package com.liferay.scalapress.folder.section

import javax.persistence.{EnumType, Enumerated, ManyToOne, JoinColumn, FetchType, Column, Table, Entity}
import scala.collection.JavaConverters._
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.enums.Sort
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFound, NotFoundAction}
import scala.util.Random
import com.sksamuel.scoot.soa.{Paging, Page}

/** @author Stephen Samuel
  *
  *         Shows a list of objects inside a folder.
  *
  **/
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
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var markup: Markup = _

    def render(sreq: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val pageNumber = sreq.param("page").getOrElse("1").toInt

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
            case Sort.Random => Random.shuffle(live)
            case _ => live.sortBy(_.name)
        }

        val page = _paginate(sorted, pageNumber, pageSize)
        val paging = Paging(sreq.request, page)

        sorted.size match {
            case 0 => Some("<!-- No objects in folder -->")
            case _ => {
                val first = sorted.head
                Option(markup).orElse(Option(first.objectType.objectListMarkup)) match {
                    case None => Some("<!-- No markup found for folder -->")
                    case Some(m) => {
                        Some(MarkupRenderer.renderObjects(sorted, m, sreq.withPaging(paging)))
                    }
                }
            }
        }
    }

    def _paginate[T](results: Iterable[T], pageNumber: Int, pageSize: Int): Page[T] = {
        require(pageNumber > 0)
        require(pageSize > 0)
        val total = results.size
        val pagedResults = results.drop((pageNumber - 1) * pageSize).take(pageSize)
        Page(pagedResults, pageNumber = pageNumber, pageSize = pageSize, totalResults = total)
    }

    def desc = "Show a paginated list of objects that are inside this folder"
    override def backoffice: String = "/backoffice/section/objectlist/" + id
}
