package com.cloudray.scalapress.util.mvc

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.plugin.form.{Submission, Form}
import com.cloudray.scalapress.plugin.ecommerce.domain.DeliveryOption
import com.cloudray.scalapress.search.{SearchFormField, SearchForm}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.obj.attr.Attribute
import com.cloudray.scalapress.user.User
import com.cloudray.scalapress.theme.{Theme, Markup}
import com.cloudray.scalapress.plugin.gallery.galleryview.Gallery

/** @author Stephen Samuel */

object UrlResolver {

  def checkout = "/checkout"
  val basket = "/basket"

  val orders = "/backoffice/order"
  def orderEdit(o: com.cloudray.scalapress.plugin.ecommerce.domain.Order) = "/backoffice/order/" + o.id
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

  @deprecated
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

  @deprecated
  def folderEdit(folder: Folder) = "/backoffice/folder/" + folder.id
  val folders = "/backoffice/folder"
  def folderImageUpload(folder: Folder): String = folderEdit(folder) + "/upload"

  @deprecated
  def folderSiteView(f: Folder): String = "/folder/" + f.id
  def folderCreate = "/backoffice/folder/create"

  @deprecated
  def objectSiteView(id: Long): String = "/object/" + id
  @deprecated
  def objectSiteView(o: Obj): String = "/object/" + o.id

  def objectCreate(t: ObjectType): String = "/backoffice/obj/create?typeId=" + t.id
  def objectEdit(o: Obj): String = "/backoffice/obj/" + Option(o).map(_.id).getOrElse("")
  def objectEdit(id: Long): String = "/backoffice/obj/" + id
  def objects(t: Long): String = "/backoffice/obj?typeId=" + t
  def objects(t: ObjectType): String = "/backoffice/obj?typeId=" + t.id
  val objects: String = "/backoffice/obj"
  def objectImageUpload(o: Obj): String = objectEdit(o) + "/upload"

  val types = "/backoffice/type"

  @deprecated
  def typeEdit(t: ObjectType) = "/backoffice/type/" + t.id

  def login = "/login"
  def logout = "/j_spring_security_logout"
}
