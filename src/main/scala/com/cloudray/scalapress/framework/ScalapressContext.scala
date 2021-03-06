package com.cloudray.scalapress.framework

import com.cloudray.scalapress.folder.{FolderPluginDao, FolderDao}
import com.cloudray.scalapress.media.{ThumbnailService, AssetStore}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.ServletContextAware
import javax.servlet.ServletContext
import com.cloudray.scalapress.settings.{GeneralSettingsDao, InstallationDao}
import com.cloudray.scalapress.theme.{ThemeService, MarkupDao}
import org.springframework.web.context.support.WebApplicationContextUtils
import com.cloudray.scalapress.item.attr.{AttributeValueDao, AttributeDao}
import com.cloudray.scalapress.payments.{PaymentPluginDao, TransactionDao}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.account.{AccountTypeDao, AccountDao}
import com.cloudray.scalapress.search.{SavedSearchDao, SearchFormDao, SearchService}
import com.cloudray.scalapress.item.{ItemTypeDao, ItemDao}
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
@Component
class ScalapressContext extends ServletContextAware {

  @Autowired var assetStore: AssetStore = _
  @Autowired var thumbnailService: ThumbnailService = _

  @Autowired var installationDao: InstallationDao = _
  @Autowired var generalSettingsDao: GeneralSettingsDao = _

  @Autowired var paymentPluginDao: PaymentPluginDao = _
  @Autowired var transactionDao: TransactionDao = _

  @Autowired var folderDao: FolderDao = _
  @Autowired var folderSettingsDao: FolderPluginDao = _

  @Autowired var searchService: SearchService = _
  @Autowired var searchFormDao: SearchFormDao = _
  @Autowired var savedSearchDao: SavedSearchDao = _

  @Autowired var attributeValueDao: AttributeValueDao = _
  @Autowired var attributeDao: AttributeDao = _

  @Autowired var itemDao: ItemDao = _
  @Autowired var itemTypeDao: ItemTypeDao = _

  @Autowired var accountDao: AccountDao = _
  @Autowired var accountTypeDao: AccountTypeDao = _

  @Autowired var markupDao: MarkupDao = _

  @Autowired var sectionDao: SectionDao = _
  @Autowired var widgetDao: WidgetDao = _

  @Autowired var orderDao: OrderDao = _

  @Autowired var themeService: ThemeService = _

  def beans[T: Manifest]: Iterable[T] = {
    WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
      .getBeansOfType(manifest.runtimeClass.asInstanceOf[Class[T]]).values.asScala
  }

  def bean[T >: Null](c: Class[T]): T = {
    try {
      WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getBean(c)
    } catch {
      case _: Exception => null
    }
  }

  def bean[T >: Null : Manifest]: T = bean[T](manifest.runtimeClass.asInstanceOf[Class[T]])

  var servletContext: ServletContext = _
  def setServletContext(servletContext: ServletContext) {
    this.servletContext = servletContext
  }
}
