package com.liferay.scalapress

import dao.settings.InstallationDao
import dao.{AttributeDao, GalleryDao, MarkupDao, FolderDao, WidgetDao, ObjectDao, TypeDao}
import plugin.ecommerce.dao.{PaymentDao, BasketDao, DeliveryOptionDao}
import plugin.ecommerce.ShoppingPluginDao
import plugin.form.{FormFieldDao, SubmissionDao, FormDao}
import plugin.listings.{ListingPackageDao, ListingsPluginDao}
import plugin.payments.paypal.standard.PaypalStandardPluginDao
import plugin.payments.sagepayform.SagepayFormPluginDao
import search.SearchService
import service.asset.AssetStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.ServletContextAware
import javax.servlet.ServletContext
import service.image.ImageService

/** @author Stephen Samuel */
@Component
class ScalapressContext extends ServletContextAware {

    @Autowired var paypalStandardPluginDao: PaypalStandardPluginDao = _
    @Autowired var sagepayFormPluginDao: SagepayFormPluginDao = _

    @Autowired var listingPackageDao: ListingPackageDao = _
    @Autowired var paymentDao: PaymentDao = _
    @Autowired var installationDao: InstallationDao = _
    @Autowired var formFieldDao: FormFieldDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @Autowired var siteDao: InstallationDao = _
    @Autowired var imageService: ImageService = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var searchService: SearchService = _
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

    var servletContext: ServletContext = _
    def setServletContext(servletContext: ServletContext) {
        this.servletContext = servletContext
    }
}
