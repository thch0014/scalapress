package com.liferay.scalapress.obj.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestParam, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import reflect.BeanProperty
import java.net.URLConnection
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.search.SearchService
import com.liferay.scalapress.section.{SectionDao, Section}
import com.liferay.scalapress.util.{EnumPopulator, ComponentClassScanner}
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.{ObjectDao, Obj}
import com.liferay.scalapress.folder.FolderDao
import com.liferay.scalapress.obj.attr.{AttributeValueDao, AttributeValue}
import com.liferay.scalapress.media.{Asset, AssetStore, Image}
import com.liferay.scalapress.util.mvc.{AttributeValuesPopulator, UrlResolver}
import com.liferay.scalapress.enums.AttributeType
import scala.collection.mutable
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeZone
import org.apache.commons.lang.WordUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/obj/{id}", "backoffice/object/{id}"))
class ObjectEditController extends FolderPopulator with AttributeValuesPopulator with EnumPopulator {

    @Autowired var assetStore: AssetStore = _
    @Autowired var attributeValueDao: AttributeValueDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var searchService: SearchService = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("form") form: EditForm) = {
        form.sellPrice = form.o.sellPrice / 100.0
        form.costPrice = form.o.costPrice / 100.0
        form.rrp = form.o.rrp / 100.0
        form.folderIds = form.o.folders.asScala.map(_.id).toArray :+ 0l
        "admin/object/edit.vm"
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("form") form: EditForm, req: HttpServletRequest) = {

        Option(form.changePassword).map(_.trim).filter(_.length > 0).foreach(pass => {
            form.o.passwordHash = passwordEncoder.encodePassword(pass, null)
        })

        form.o.sellPrice = (form.sellPrice * 100).toInt
        form.o.costPrice = (form.costPrice * 100).toInt
        form.o.rrp = (form.rrp * 100).toInt

        form.o.folders.asScala.foreach(folder => folder.objects.remove(form.o))
        form.o.folders.clear()
        for ( id <- form.folderIds ) {
            val folder = folderDao.find(id)
            if (folder != null) {
                form.o.folders.add(folder)
                folder.objects.add(form.o)
            }
        }

        form.o.attributeValues.clear()
        for ( a <- form.o.objectType.attributes.asScala ) {
            val values = req.getParameterValues("attributeValues" + a.id)
            if (values != null) {
                values.map(_.trim)
                  .filter(!_.isEmpty)
                  .map(value => {
                    // some values need to be converted first
                    a.attributeType match {
                        case AttributeType.Date =>
                            try {
                                DateTimeFormat.forPattern("dd-MM-yyyy")
                                  .withZone(DateTimeZone.UTC).parseMillis(value).toString
                            } catch {
                                case e: Exception => null
                            }
                        case _ => value
                    }
                }).filter(_ != null)
                  .foreach(value => {
                    val av = new AttributeValue
                    av.attribute = a
                    av.obj = form.o
                    av.value = value
                    form.o.attributeValues.add(av)
                })
            }
        }

        if (form.upload != null) {
            if (!form.upload.isEmpty) {
                val key = assetStore.add(form.upload.getOriginalFilename, form.upload.getInputStream)
                val image = Image(key)
                image.obj = form.o
                form.o.images.add(image)
            }
        }

        if (form.associatedObjectId != null && form.associatedObjectId.trim.length > 0)
            form.o.associations.add(objectDao.find(form.associatedObjectId.trim.toLong))

        objectDao.save(form.o)
        if (!form.o.objectType.name.toLowerCase.contains("account"))
            searchService.index(form.o)
        "redirect:/backoffice/obj/" + form.o.id
    }

    @RequestMapping(Array("section/create"))
    def createSection(@ModelAttribute("form") form: EditForm, @RequestParam("class") cls: String) = {
        val section = Class.forName(cls).newInstance.asInstanceOf[Section]
        section.obj = form.o
        section.visible = true
        section.init(context)
        form.o.sections.add(section)
        objectDao.save(form.o)
        "redirect:/backoffice/obj/" + form.o.id
    }

    @RequestMapping(value = Array("/section/order"), method = Array(RequestMethod.POST))
    def reorderSections(@RequestBody order: String, @ModelAttribute("form") form: EditForm): String = {

        val ids = order.split("-")
        form.o.sections.asScala.foreach(section => {
            val pos = ids.indexOf(section.id.toString)
            section.position = pos
            sectionDao.save(section)
        })
        "ok"
    }

    @RequestMapping(value = Array("association/{associationId}/delete"))
    def deleteAssociation(@ModelAttribute("form") form: EditForm,
                          @PathVariable("associationId") associationId: Long): String = {
        form.o.associations.asScala.find(_.id == associationId) match {
            case None =>
            case Some(association) =>
                form.o.associations.remove(association)
                objectDao.save(form.o)
        }
        "redirect:/backoffice/obj/" + form.o.id
    }

    @RequestMapping(value = Array("section/{sectionId}/delete"))
    def deleteSection(@ModelAttribute("form") form: EditForm, @PathVariable("sectionId") sectionId: Long): String = {
        form.o.sections.asScala.find(_.id == sectionId) match {
            case None =>
            case Some(section) =>
                form.o.sections.remove(section)
                section.obj = null
                objectDao.save(form.o)
        }
        "redirect:/backoffice/obj/" + form.o.id
    }

    @RequestMapping(Array("image/{filename}/remove"))
    def removeImage(@PathVariable("filename") filename: String,
                    @ModelAttribute("form") form: EditForm) = {
        form.o.images.asScala.find(_.filename == filename) match {
            case None =>
            case Some(img) =>
                form.o.images.remove(img)
                img.obj = null
                objectDao.save(form.o)
        }
        "redirect:/backoffice/obj/" + form.o.id
    }

    @ModelAttribute("assets") def assets(@PathVariable("id") id: Long): java.util.Collection[Asset] = {
        val obj = objectDao.find(id)
        val java = obj.images.asScala.map(img => {
            Asset(img.filename,
                0,
                assetStore.link(img.filename),
                URLConnection.guessContentTypeFromName(img.filename))
        }).asJava
        java
    }

    @ModelAttribute("booleanMap") def booleanMap = Map("Yes" -> "Yes", "No" -> "No").asJava

    @ModelAttribute("statuses") def statuses =
        Map("Live" -> "Live", "Disabled" -> "Disabled", "Deleted" -> "Deleted").asJava

    @ModelAttribute def f(@PathVariable("id") id: Long, model: ModelMap) {
        val obj = objectDao.find(id)
        val form = new EditForm
        form.o = obj
        form.folderIds = Array()

        // fix status so it matches the title case of the selects
        obj.status = WordUtils.capitalizeFully(obj.status)

        model.put("attributesWithValues",
            attributeEditMap(obj.objectType.attributes.asScala.toSeq, obj.attributeValues.asScala.toSeq))
        model.put("form", form)
        model.put("eyeball", UrlResolver.objectSiteView(obj))

        val sections = obj.sections.asScala.toSeq.sortBy(_.position).asJava
        model.put("sections", sections)
    }

    @ModelAttribute("installation") def installation = context.installationDao.get

    @ModelAttribute("classes") def classes: java.util.Map[String, String] = {
        val sections = ComponentClassScanner.sections.sortBy(_.getSimpleName)
        val empty = mutable.LinkedHashMap.empty[String, String]
        val map = empty ++ sections.map(c => (c.getName, c.getSimpleName)).toMap
        map.asJava
    }
}

class EditForm {
    @BeanProperty var associatedObjectId: String = _
    @BeanProperty var sellPrice: Double = _
    @BeanProperty var costPrice: Double = _
    @BeanProperty var rrp: Double = _
    @BeanProperty var o: Obj = _
    @BeanProperty var changePassword: String = _
    @BeanProperty var folderIds: Array[Long] = _
    @BeanProperty var upload: MultipartFile = _
}

