package com.cloudray.scalapress.folder.section

import javax.persistence.{EnumType, Enumerated, ManyToOne, JoinColumn, FetchType, Column, Table, Entity}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFound, NotFoundAction}
import com.sksamuel.scoot.soa.{Paging, Page}
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.search.{Sort, PagingRenderer}
import com.cloudray.scalapress.obj.{ObjectSorter, Obj}
import com.cloudray.scalapress.obj.attr.Attribute

/** @author Stephen Samuel
  *
  *         Shows a list of objects inside a folder.
  *
  * */
@Entity
@Table(name = "blocks_items")
class ObjectListSection extends Section {

  def desc = "Show a paginated list of objects that are inside this folder"
  override def backoffice: String = "/backoffice/section/objectlist/" + id

  @Enumerated(value = EnumType.STRING)
  @Column(name = "sortType")
  @BeanProperty var sort: Sort = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sortAttribute")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var sortAttribute: Attribute = _

  @Column(name = "itemsPerPage")
  @BeanProperty var pageSize: Int = ObjectListSection.PAGE_SIZE_DEFAULT

  @Column(name = "includeSubcategoryItems")
  @BeanProperty var includeSubfolderObjects: Boolean = false

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "listMarkup", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var markup: Markup = _

  def _objects(sreq: ScalapressRequest): Seq[Obj] = {

    val objects = try {
      folder.objects.asScala.toSeq
    } catch {
      case e: Exception => Nil
    }

    val live = objects.filter(_.status.toLowerCase == Obj.STATUS_LIVE.toLowerCase)
    val sorted = ObjectSorter.sort(live, _sort(sreq.context), Option(sortAttribute), sreq.sessionId.hashCode)
    sorted
  }

  def render(sreq: ScalapressRequest): Option[String] = {

    val pageNumber = sreq.param("pageNumber").filter(_.forall(_.isDigit)).getOrElse("1").toInt
    val objects = _objects(sreq)

    val safePageSize = _pageSize(sreq.context)
    val usePaging = objects.size > safePageSize
    val page = _paginate(objects, pageNumber, safePageSize)
    val paging = Paging(sreq.request, page)

    val renderedObjects = page.results.size match {
      case 0 => "<!-- No objects in folder -->"
      case _ => {
        val objectTypeMarkup = Option(page.results.head.objectType.objectListMarkup)
        val markupToUse = Option(markup).orElse(objectTypeMarkup)
        markupToUse match {
          case Some(m) => MarkupRenderer.renderObjects(page.results, m, sreq.withPaging(paging))
          case None => "<!-- No markup found for folder -->"
        }
      }
    }

    val buffer = new ListBuffer[String]
    buffer.append(renderedObjects)
    if (usePaging) {
      buffer.prepend(PagingRenderer.render(paging))
      buffer.append(PagingRenderer.render(paging))
    }

    Some(buffer.mkString("\n"))
  }

  def _paginate[T](results: Iterable[T], pageNumber: Int, pageSize: Int): Page[T] = {
    require(pageNumber > 0)
    require(pageSize > 0)
    val total = results.size
    val pagedResults = results.drop((pageNumber - 1) * pageSize).take(pageSize)
    Page(pagedResults, pageNumber = pageNumber, pageSize = pageSize, totalResults = total)
  }

  def _sort(context: ScalapressContext): Sort = Option(sort).getOrElse(context.folderSettingsDao.head.sort)
  def _pageSize(context: ScalapressContext): Int = {
    if (pageSize > 0)
      pageSize
    else {
      val settingsPageSize = context.folderSettingsDao.head.pageSize
      if (settingsPageSize > 0)
        settingsPageSize
      else
        ObjectListSection.PAGE_SIZE_DEFAULT
    }
  }

}

object ObjectListSection {
  val PAGE_SIZE_DEFAULT = 50
}