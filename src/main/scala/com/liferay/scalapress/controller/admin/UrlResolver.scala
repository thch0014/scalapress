package com.liferay.scalapress.controller.admin

import com.liferay.scalapress.domain.{Gallery, User, Markup, ObjectType, Obj, Folder}
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.domain.setup.Theme
import com.liferay.scalapress.domain.form.{Form, Submission}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.domain.attr.Attribute

/** @author Stephen Samuel */

object UrlResolver {

    val basket = "basket"
    def addToBasket(obj: Obj) = "basket/add/" + obj.id

    def userEdit(user: User) = "/backoffice/user/" + user.id
    var users = "/backoffice/user"
    def createUser = "/backoffice/user/create"

    def viewSubmission(s: Submission) = "/backoffice/submission/" + s.id
    def submissions = "/backoffice/submission"
    def forms = "/backoffice/form"
    def editForm(form: Form) = "/backoffice/form/" + form.id

    val dashboard: String = "/backoffice"

    val medialib: String = "/backoffice/medialib"
    val mediaupload: String = "/backoffice/medialib/upload"

    val createTheme = "/backoffice/theme/create"
    val themes = "/backoffice/theme/"
    def themeEdit(t: Theme) = "/backoffice/theme/" + t.id

    val galleries = "/backoffice/gallery"
    def galleryEdit(g: Gallery) = "/backoffice/gallery/" + g.id
    def galleryCreate = "/backoffice/gallery/create"

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

    def folderEdit(folder: Folder) = "/backoffice/folder/" + folder.id
    val folders = "/backoffice/folder"
    def folderImageUpload(folder: Folder): String = folderEdit(folder) + "/upload"
    def folderSiteView(f: Folder): String = "/folder/" + f.id
    def folderCreate = "/backoffice/folder/create"

    def objectSiteView(o: Obj): String = "/object/" + o.id
    def objectCreate(t: ObjectType) = "/backoffice/obj/create?typeId=" + t.id
    def objectEdit(o: Obj) = "/backoffice/obj/" + Option(o).map(_.id).getOrElse("")
    def objects(t: Long) = "/backoffice/obj?typeId=" + t
    def objects(t: ObjectType) = "/backoffice/obj?typeId=" + t.id
    val objects = "/backoffice/obj"
    def objectImageUpload(o: Obj): String = objectEdit(o) + "/upload"

    val types = "/backoffice/type"
    def typeEdit(t: ObjectType) = "/backoffice/type/" + t.id

    def addAttribute(t: ObjectType) = typeEdit(t) + "/attribute/create"

    def login = "/login"
    def logout = "/j_spring_security_logout"
}
