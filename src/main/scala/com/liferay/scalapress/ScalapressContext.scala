package com.liferay.scalapress

import com.liferay.scalapress.folder.{FolderPluginDao, FolderDao}
import media.{AssetStore, ImageService, GalleryDao}
import obj.{ObjectDao, TypeDao}
import plugin.ecommerce.dao.{TransactionDao, BasketDao, DeliveryOptionDao}
import plugin.ecommerce.{OrderDao, ShoppingPluginDao}
import plugin.form.{FormFieldDao, SubmissionDao, FormDao}
import plugin.listings.{ListingProcessDao, ListingPackageDao, ListingsPluginDao}
import plugin.payments.PaymentPluginDao
import plugin.payments.paypal.standard.PaypalStandardPluginDao
import plugin.payments.sagepayform.SagepayFormPluginDao
import search.{SavedSearchDao, SearchFormDao, SearchService}
import section.SectionDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.ServletContextAware
import javax.servlet.ServletContext
import settings.InstallationDao
import theme.MarkupDao
import widgets.WidgetDao
import org.springframework.web.context.support.WebApplicationContextUtils
import com.liferay.scalapress.obj.attr.{AttributeValueDao, AttributeDao}
import com.liferay.scalapress.plugin.profile.AccountPluginDao

/** @author Stephen Samuel */
@Component
class ScalapressContext extends ServletContextAware {

    @Autowired var folderPluginDao: FolderPluginDao = _
    @Autowired var sectionDao: SectionDao = _

    @Autowired var paymentPluginDao: PaymentPluginDao = _
    @Autowired var paypalStandardPluginDao: PaypalStandardPluginDao = _
    @Autowired var sagepayFormPluginDao: SagepayFormPluginDao = _
    @Autowired var accountPluginDao: AccountPluginDao = _

    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _

    @Autowired var transactionDao: TransactionDao = _
    @Autowired var installationDao: InstallationDao = _

    @Autowired var orderDao: OrderDao = _

    @deprecated
    def siteDao = installationDao

    @Autowired var formFieldDao: FormFieldDao = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @Autowired var imageService: ImageService = _
    @Autowired var basketDao: BasketDao = _

    @Autowired var searchService: SearchService = _
    @Autowired var searchFormDao: SearchFormDao = _
    @Autowired var savedSearchDao: SavedSearchDao = _

    @Autowired var attributeDao: AttributeDao = _
    @Autowired var widgetDao: WidgetDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var galleryDao: GalleryDao = _
    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var formDao: FormDao = _
    @Autowired var attributeValueDao: AttributeValueDao = _

    def bean[T](implicit m: Manifest[T]) =
        WebApplicationContextUtils
          .getRequiredWebApplicationContext(servletContext)
          .getBean(m.erasure.asInstanceOf[Class[T]])

    var servletContext: ServletContext = _
    def setServletContext(servletContext: ServletContext) {
        this.servletContext = servletContext
    }
}
