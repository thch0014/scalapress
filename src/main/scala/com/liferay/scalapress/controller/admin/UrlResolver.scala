package com.liferay.scalapress.controller.admin

import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.plugin.gallery.Gallery
import com.liferay.scalapress.plugin.form.{Submission, Form}
import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption
import com.liferay.scalapress.search.{SearchFormField, SearchForm}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.obj.{ObjectType, Obj}
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.obj.attr.Attribute
import com.liferay.scalapress.settings.Theme
import com.liferay.scalapress.user.User
import com.liferay.scalapress.theme.Markup

/** @author Stephen Samuel */

object UrlResolver {

    def checkout = "/checkout"
    val basket = "/basket"
    def addToBasket(obj: Obj) = "/basket/add/" + obj.id

    val orders = "/backoffice/order"
    def orderEdit(o: com.liferay.scalapress.plugin.ecommerce.domain.Order) = "/backoffice/order/" + o.id
    val createOrder = "/backoffice/order/create"

    def userEdit(user: User) = "/backoffice/user/" + user.id
    val users = "/backoffice/user"
    val createUser = "/backoffice/user/create"

    def viewSubmission(s: Submission) = "/backoffice/submission/" + s.id
    def submissions = "/backoffice/submission"
    def forms = "/backoffice/form"
    def editForm(form: Form) = "/backoffice/form/" + form.id

    def searchForms = "/backoffice/searchform"
    def editSearchForm(f: SearchForm) = "/backoffice/searchform/" + f.id
    def createSearchForm = "/backoffice/searchform/create"
    def editSearchFormField(f: SearchFormField) = editSearchForm(f.searchForm) + "/field/" + f.id

    def deliveryOptions = "/backoffice/delivery"
    def editDeliveryOption(o: DeliveryOption) = "/backoffice/delivery/" + o.id

    val dashboard: String = "/backoffice"

    val medialib: String = "/backoffice/medialib"
    val mediaupload: String = "/backoffice/medialib/upload"

    val createTheme = "/backoffice/theme/create"
    val themes = "/backoffice/theme/"
    def themeEdit(t: Theme) = "/backoffice/theme/" + t.id

    val galleries = "/backoffice/gallery"
    def galleryEdit(g: Gallery) = "/backoffice/gallery/" + g.id
    def galleryCreate = "/backoffice/gallery/create"
    def galleryView(g: Gallery) = "/gallery/" + g.id

    val widgets = "/backoffice/widget/"
    def widgetEdit(w: Widget) = "/backoffice/widget/" + w.id

    def deleteSection(folder: Folder, s: Section) = folderEdit(folder) + "/section/" + s.id + "/delete"
    val plugins = "/backoffice/section"
    def pluginEdit(p: Section) = "/backoffice/section/" + p.id

    val markups = "/backoffice/markup/"
    def markupEdit(m: Markup) = "/backoffice/markup/" + m.id

    val installationSettings = "/backoffice/settings/installation"
    def generalSettings = "/backoffice/settings/site"

    def attributeEdit(a: Attribute) = "/backoffice/attribute/" + a.id
    def createAttribute(t: ObjectType) = typeEdit(t) + "/attribute/create"

    def folderEdit(folder: Folder) = "/backoffice/folder/" + folder.id
    val folders = "/backoffice/folder"
    def folderImageUpload(folder: Folder): String = folderEdit(folder) + "/upload"
    def folderSiteView(f: Folder): String = "/folder/" + f.id
    def folderCreate = "/backoffice/folder/create"

    def objectSiteView(id: Long): String = "/object/" + id
    def objectSiteView(o: Obj): String = "/object/" + o.id
    def objectCreate(t: ObjectType): String = "/backoffice/obj/create?typeId=" + t.id
    def objectEdit(o: Obj): String = "/backoffice/obj/" + Option(o).map(_.id).getOrElse("")
    def objectEdit(id: Long): String = "/backoffice/obj/" + id
    def objects(t: Long): String = "/backoffice/obj?typeId=" + t
    def objects(t: ObjectType): String = "/backoffice/obj?typeId=" + t.id
    val objects: String = "/backoffice/obj"
    def objectImageUpload(o: Obj): String = objectEdit(o) + "/upload"

    val types = "/backoffice/type"
    def typeEdit(t: ObjectType) = "/backoffice/type/" + t.id

    def login = "/login"
    def logout = "/j_spring_security_logout"
}
