package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{AttributeValueDao, FolderDao, ObjectDao}
import scala.Array
import com.liferay.scalapress.domain.{Image, Obj}
import com.liferay.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.service.asset.{Asset, AssetStore}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import reflect.BeanProperty
import java.net.URLConnection
import com.liferay.scalapress.plugin.search.SearchService
import com.liferay.scalapress.domain.attr.{AttributeValue, Attribute}
import org.springframework.security.authentication.encoding.PasswordEncoder

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/obj/{id}", "backoffice/object/{id}"))
class ObjectEditController extends FolderPopulator {

    @Autowired var assetStore: AssetStore = _
    @Autowired var attributeValueDao: AttributeValueDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var searchService: SearchService = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("form") form: EditForm) = {
        form.sellPrice = form.o.sellPrice / 100.0
        form.costPrice = form.o.costPrice / 100.0
        form.rrp = form.o.rrp / 100.0
        form.folderIds = form.o.folders.asScala.map(_.id).toArray :+ 0l
        "admin/object/edit.vm"
    }

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("form") form: EditForm, req: HttpServletRequest) = {

        Option(form.changePassword).map(_.trim).filter(_.length > 0).foreach(pass => {
            form.o.passwordHash = passwordEncoder.encodePassword(pass, null)
        })

        form.o.sellPrice = (form.sellPrice * 100).toInt
        form.o.costPrice = (form.costPrice * 100).toInt
        form.o.rrp = (form.rrp * 100).toInt

        form.o.folders.asScala.foreach(folder => folder.objects.remove(form.o))
        form.o.folders.clear()
        for (id <- form.folderIds) {
            val folder = folderDao.find(id)
            if (folder != null) {
                form.o.folders.add(folder)
                folder.objects.add(form.o)
            }
        }

        form.o.attributeValues.clear()

        for (a <- form.o.objectType.attributes.asScala) {

            val values = req.getParameterValues("attributeValues" + a.id)
            if (values != null) {
                values.map(_.trim).filter(_.length > 0).foreach(value => {
                    val av = new AttributeValue
                    av.attribute = a
                    av.value = value
                    av.obj = form.o
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

        objectDao.save(form.o)
        searchService.index(form.o)
        searchService.index(form.o)
        "redirect:/backoffice/obj/" + form.o.id
    }

    @ModelAttribute("attributesWithValues")
    def attributeEditMap(@PathVariable("id") id: Long): java.util.List[(Attribute, java.util.List[String])] = {
        val obj = objectDao.find(id)
        val attributes = obj.objectType.attributes.asScala.sortWith((a, b) => {
            val comp = Option(a.section).getOrElse("").compareTo(Option(b.section).getOrElse(""))
            if (comp == 0)
                a.position.compareTo(b.position) < 0
            else
                comp < 0
        })
        val attributesWithValues = attributes.map(a => {
            var values = obj.attributeValues.asScala.filter(_.attribute.id == a.id).map(_.value)
            if (values.isEmpty || a.multipleValues)
                values = values :+ ""
            (a, values.asJava)
        })
        attributesWithValues.asJava
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
        form

        model.put("form", form)
        model.put("eyeball", UrlResolver.objectSiteView(obj))
    }
}

class EditForm {

    @BeanProperty var sellPrice: Double = _
    @BeanProperty var costPrice: Double = _
    @BeanProperty var rrp: Double = _
    @BeanProperty var o: Obj = _
    @BeanProperty var changePassword: String = _
    @BeanProperty var folderIds: Array[Long] = _
    @BeanProperty var upload: MultipartFile = _
}

