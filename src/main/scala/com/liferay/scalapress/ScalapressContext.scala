package com.liferay.scalapress

import com.liferay.scalapress.folder.{FolderPluginDao, FolderDao}
import media.{AssetStore, ImageService}
import obj.{ObjectDao, TypeDao}
import plugin.ecommerce.dao.{BasketDao, DeliveryOptionDao}
import plugin.ecommerce.{OrderDao, ShoppingPluginDao}
import plugin.form.FormFieldDao
import com.liferay.scalapress.plugin.listings.{ListingsPluginDao, ListingProcessDao, ListingPackageDao}
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
import com.liferay.scalapress.payments.{PaymentPluginDao, TransactionDao}

/** @author Stephen Samuel */
@Component
class ScalapressContext extends ServletContextAware {

    @Autowired var transactionDao: TransactionDao = _

    @Autowired var folderSettingsDao: FolderPluginDao = _
    @Autowired var sectionDao: SectionDao = _

    @Autowired var paymentPluginDao: PaymentPluginDao = _

    @deprecated
    @Autowired var paypalStandardPluginDao: PaypalStandardPluginDao = _
    @deprecated
    @Autowired var sagepayFormPluginDao: SagepayFormPluginDao = _
    @deprecated
    @Autowired var accountPluginDao: AccountPluginDao = _

    @deprecated
    @Autowired var listingPackageDao: ListingPackageDao = _
    @deprecated
    @Autowired var listingProcessDao: ListingProcessDao = _
    @deprecated
    @Autowired var listingsPluginDao: ListingsPluginDao = _

    @Autowired var installationDao: InstallationDao = _

    @deprecated
    @Autowired var orderDao: OrderDao = _

    @deprecated
    def siteDao = installationDao

    @deprecated
    @Autowired var formFieldDao: FormFieldDao = _
    @deprecated
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @Autowired var imageService: ImageService = _

    @deprecated
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

    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var markupDao: MarkupDao = _

    @Autowired var attributeValueDao: AttributeValueDao = _

    def bean[T](c: Class[T]): T = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getBean(c)
    def bean[T](implicit m: Manifest[T]): T = bean[T](manifest.runtimeClass.asInstanceOf[Class[T]])

    var servletContext: ServletContext = _
    def setServletContext(servletContext: ServletContext) {
        this.servletContext = servletContext
    }
}
