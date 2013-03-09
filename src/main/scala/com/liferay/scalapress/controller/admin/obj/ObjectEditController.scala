package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestParam, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{FolderDao, ObjectDao}
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

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/obj/{id}"))
class ObjectEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("form") form: EditForm) = "admin/object/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("form") form: EditForm, req: HttpServletRequest) = {

        form.o.folders.clear()
        for (id <- form.folderIds) {
            val folder = folderDao.find(id)
            if (folder != null) {
                form.o.folders.add(folder)
            }
        }

        //        val attrValues = new ArrayBuffer[AttributeValue]
        //        for (a <- form.o.objectType.attributes.asScala) {
        //
        //            val values = req.getParameterValues("attribute_value_" + a.id)
        //            if (values != null) {
        //                values.map(_.trim).filter(_.length > 0).foreach(value => {
        //                    val av = new AttributeValue
        //                    av.attribute = a
        //                    av.value = value
        //                    av.obj = form.o
        //                    attrValues += av
        //                })
        //            }
        //        }
        //        form.o.attributeValues = attrValues.toList.asJava

        if (form.upload != null) {
            val key = assetStore.put(form.upload.getOriginalFilename, form.upload.getInputStream)

            val image = new Image
            image.filename = key
            image.date = System.currentTimeMillis()
            image.obj = form.o
            form.o.images.add(image)
        }

        objectDao.save(form.o)
        edit(form)
    }

    @ModelAttribute("attributes") def attr(@PathVariable("id") id: Long) = {
        val obj = objectDao.find(id)
        obj.objectType
          .attributes
          .asScala
          .sortBy(_.section)
          .asJava
    }

    @ModelAttribute("folders") def folder = {
        val folders = folderDao.findAll().map(f => (f.id, f.fullName))
        val options = (0, "-None-") +: folders
        val java = options.toMap.asJava
        java
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

    @ModelAttribute("statuses") def statuses =
        Map("Live" -> "Live", "Disabled" -> "Disabled", "Deleted" -> "Deleted").asJava

    @ModelAttribute("form") def form(@PathVariable("id") id: Long, model: ModelMap): EditForm = {
        val obj = objectDao.find(id)
        val form = new EditForm
        form.o = obj
        form.folderIds = Array()
        form
    }
}

class EditForm {
    @BeanProperty var o: Obj = _
    @BeanProperty var folderIds: Array[Long] = _
    @BeanProperty var upload: MultipartFile = _
}
