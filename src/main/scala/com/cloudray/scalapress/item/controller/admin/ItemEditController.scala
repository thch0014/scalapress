package com.cloudray.scalapress.item.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.ui.ModelMap
import java.net.URLConnection
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.section.{SectionDao, Section}
import com.cloudray.scalapress.util.EnumPopulator
import scala.collection.JavaConverters._
import com.cloudray.scalapress.item.{ItemCloner, ItemDao, Item}
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.item.attr.{AttributeType, AttributeValueDao, AttributeValue}
import com.cloudray.scalapress.media._
import com.cloudray.scalapress.util.mvc.AttributeValuesPopulator
import scala.collection.mutable
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeZone
import org.apache.commons.lang.WordUtils
import com.cloudray.scalapress.folder.controller.admin.SectionSorting
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.listings.ListingPackageDao
import com.cloudray.scalapress.account.AccountDao
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressContext, ComponentClassScanner}
import com.cloudray.migration.ImageDao
import com.cloudray.scalapress.media.Asset
import scala.Some

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/obj/{id}", "backoffice/object/{id}", "backoffice/item/{id}"))
class ItemEditController(val assetStore: AssetStore,
                         val attributeValueDao: AttributeValueDao,
                         val itemDao: ItemDao,
                         val folderDao: FolderDao,
                         val imageDao: ImageDao,
                         val accountDao: AccountDao,
                         val sectionDao: SectionDao,
                         val context: ScalapressContext,
                         val searchService: SearchService,
                         val passwordEncoder: PasswordEncoder)
  extends FolderPopulator with AttributeValuesPopulator with EnumPopulator with SectionSorting {

  @RequestMapping(Array("clone"))
  def clone(@ModelAttribute("form") form: EditForm) = {
    val clone = new ItemCloner().clone(form.o)
    itemDao.save(clone)
    "redirect:/backoffice/item/" + clone.id
  }

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("form") form: EditForm) = {
    form.sellPrice = form.o.price / 100.0
    form.costPrice = form.o.costPrice / 100.0
    form.rrp = form.o.rrp / 100.0
    form.folderIds = (form.o.folders.asScala.map(_.id) + 0l).toArray
    "admin/item/edit.vm"
  }

  def _attributeValueNormalize(value: String, attributeType: AttributeType) = {
    attributeType match {
      case AttributeType.Date =>
        try {
          DateTimeFormat.forPattern("dd-MM-yyyy").withZone(DateTimeZone.UTC).parseMillis(value).toString
        } catch {
          case e: Exception => null
        }
      case AttributeType.Numerical => value.replaceAll("[^\\d.]", "")
      case _ => value
    }
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("form") form: EditForm, req: HttpServletRequest) = {

    form.o.price = (form.sellPrice * 100).toInt
    form.o.costPrice = (form.costPrice * 100).toInt
    form.o.rrp = (form.rrp * 100).toInt
    form.o.expiry = Option(form.expiry)
      .map(arg => DateTimeFormat.forPattern("dd-MM-yyyy")
      .parseLocalDate(arg)
      .toDateTimeAtStartOfDay(DateTimeZone.UTC)
      .getMillis)
      .getOrElse(0)

    try {
      form.o.account = accountDao.find(form.account.toLong)
    } catch {
      case e: Exception =>
    }

    form.o.folders.asScala.foreach(folder => folder.items.remove(form.o))
    form.o.folders.clear()
    for ( id <- form.folderIds ) {
      val folder = folderDao.find(id)
      if (folder != null) {
        form.o.folders.add(folder)
        folder.items.add(form.o)
      }
    }

    form.o.attributeValues.clear()
    for ( a <- form.o.itemType.attributes.asScala ) {
      val values = req.getParameterValues("attributeValues" + a.id)
      if (values != null) {
        values.map(_.trim)
          .filter(!_.isEmpty)
          .map(value => _attributeValueNormalize(value, a.attributeType))
          .filter(_ != null)
          .foreach(value => {
          val av = new AttributeValue
          av.attribute = a
          av.item = form.o
          av.value = value
          form.o.attributeValues.add(av)
        })
      }
    }

    if (form.upload != null) {
      if (!form.upload.isEmpty) {
        val key = assetStore.add(form.upload.getOriginalFilename, form.upload.getInputStream)
        form.o.images.add(key)
      }
    }

    if (form.associatedObjectId != null && form.associatedObjectId.trim.length > 0)
      form.o.associations.add(itemDao.find(form.associatedObjectId.trim.toLong))

    itemDao.save(form.o)
    //searchService.index(Seq(form.o))

    "redirect:/backoffice/item/" + form.o.id
  }

  @RequestMapping(Array("section/create"))
  def createSection(@ModelAttribute("form") form: EditForm, @RequestParam("class") cls: String) = {
    val section = Class.forName(cls).newInstance.asInstanceOf[Section]
    section.item = form.o
    section.visible = true
    section.init(context)
    form.o.sections.add(section)
    itemDao.save(form.o)
    "redirect:/backoffice/item/" + form.o.id
  }

  @ResponseBody
  @RequestMapping(value = Array("/image/order"), method = Array(RequestMethod.POST))
  def reorderImages(@RequestBody order: String,
                    @ModelAttribute("form") form: EditForm,
                    resp: HttpServletResponse) {
    val ids = order.split("-")
    if (ids.size == form.o.images.size)
      form.o.images.asScala.foreach(img => {
        //        val pos = ids.indexOf(img.id.toString)
        //      img.position = pos
        //    imageDao.save(img)
      })
    resp.setStatus(HttpServletResponse.SC_OK)
  }

  @ResponseBody
  @RequestMapping(value = Array("/section/order"), method = Array(RequestMethod.POST))
  def reorderSections(@RequestBody order: String,
                      @ModelAttribute("form") form: EditForm,
                      resp: HttpServletResponse) {
    reorderSections(order, form.o.sections.asScala)
    resp.setStatus(HttpServletResponse.SC_OK)
  }

  @RequestMapping(value = Array("association/{associationId}/delete"))
  def deleteAssociation(@ModelAttribute("form") form: EditForm,
                        @PathVariable("associationId") associationId: Long): String = {
    form.o.associations.asScala.find(_.id == associationId) match {
      case None =>
      case Some(association) =>
        form.o.associations.remove(association)
        itemDao.save(form.o)
    }
    "redirect:/backoffice/item/" + form.o.id
  }

  @RequestMapping(value = Array("section/{sectionId}/delete"))
  def deleteSection(@ModelAttribute("form") form: EditForm, @PathVariable("sectionId") sectionId: Long): String = {
    form.o.sections.asScala.find(_.id == sectionId) match {
      case None =>
      case Some(section) =>
        form.o.sections.remove(section)
        section.item = null
        itemDao.save(form.o)
    }
    "redirect:/backoffice/item/" + form.o.id + "#tab3"
  }

  @RequestMapping(Array("image/{filename}/remove"))
  def removeImage(@PathVariable("filename") filename: String,
                  @ModelAttribute("form") form: EditForm) = {
    if (form.o.images.remove(filename)) itemDao.save(form.o)
    "redirect:/backoffice/item/" + form.o.id + "#tab5"
  }

  @ModelAttribute("assets")
  def assets(@PathVariable("id") id: Long): java.util.Collection[Asset] = {
    val item = itemDao.find(id)
    val java = item.images.asScala.map(img => {
      Asset(img, 0, assetStore.link(img), URLConnection.guessContentTypeFromName(img))
    }).asJava
    java
  }

  @ModelAttribute("booleanMap")
  def booleanMap = Map("Yes" -> "Yes", "No" -> "No").asJava

  @ModelAttribute("statuses")
  def statuses =
    Map(Item.STATUS_LIVE -> Item.STATUS_LIVE,
      Item.STATUS_DISABLED -> Item.STATUS_DISABLED,
      Item.STATUS_DELETED -> Item.STATUS_DELETED).asJava

  @ModelAttribute
  def f(@PathVariable("id") id: Long, model: ModelMap) {
    val item = itemDao.find(id)
    val form = new EditForm
    form.o = item
    form.folderIds = Array()

    // fix status so it matches the title case of the selects
    item.status = WordUtils.capitalizeFully(item.status)

    model.put("attributesWithValues",
      attributeEditMap(item.itemType.sortedAttributes, item.attributeValues.asScala.toSeq))
    model.put("form", form)
    model.put("eyeball", UrlGenerator.url(item))

    val sections = item.sections.asScala.toSeq.sortBy(_.position).asJava
    model.put("sections", sections)
  }

  @ModelAttribute("installation") def installation = context.installationDao.get
  @ModelAttribute("listingsEnabled") def listingsEnabled = context.bean[ListingPackageDao].enabled

  @ModelAttribute("classes")
  def classes: java.util.Map[String, String] = {
    val sections = ComponentClassScanner.sections.sortBy(_.getSimpleName)

    val map = mutable.LinkedHashMap.empty[String, String]
    sections.foreach(section => {
      map.put(section.getName, section.getSimpleName)
    })

    map.asJava
  }
}

class EditForm {
  @BeanProperty var associatedObjectId: String = _
  @BeanProperty var expiry: String = _
  @BeanProperty var sellPrice: Double = _
  @BeanProperty var costPrice: Double = _
  @BeanProperty var rrp: Double = _
  @BeanProperty var o: Item = _
  @BeanProperty var account: String = _
  @BeanProperty var folderIds: Array[Long] = _
  @BeanProperty var upload: MultipartFile = _
}

