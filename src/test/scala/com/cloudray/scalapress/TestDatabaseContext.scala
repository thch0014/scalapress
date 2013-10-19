package com.cloudray.scalapress

import org.springframework.context.support.ClassPathXmlApplicationContext
import com.cloudray.scalapress.payments.{TransactionDao, TransactionDaoImpl}
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.theme.{MarkupDao, MarkupDaoImpl, ThemeDao, ThemeDaoImpl}
import com.cloudray.scalapress.search.{SearchFormDao, SearchFormDaoImpl, SavedSearchDao, SavedSearchDaoImpl}
import com.cloudray.scalapress.plugin.ecommerce.{OrderDao, OrderDaoImpl}
import com.cloudray.scalapress.plugin.listings.{ListingProcessDao, ListingProcessDaoImpl, ListingPackageDao, ListingPackageDaoImpl}
import com.cloudray.scalapress.plugin.form.{FormFieldDao, FormFieldDaoImpl, FormDao, FormDaoImpl}
import com.cloudray.scalapress.obj.{TypeDao, TypeDaoImpl, ObjectDao, ObjectDaoImpl}
import com.cloudray.scalapress.user.{UserDao, UserDaoImpl}
import com.cloudray.scalapress.folder.{FolderDao, FolderDaoImpl}
import org.hibernate.SessionFactory
import com.cloudray.scalapress.section.{SectionDao, SectionDaoImpl}
import com.cloudray.scalapress.obj.attr.{AttributeValueDao, AttributeValueDaoImpl, AttributeDao, AttributeDaoImpl}
import com.cloudray.scalapress.plugin.account.{AccountPluginDao, AccountPluginDaoImpl}
import com.cloudray.scalapress.plugin.variations._

/** @author Stephen Samuel */
object TestDatabaseContext {

  val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

  val attributeDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[AttributeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[AttributeDao]

  val attributeValueDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[AttributeValueDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[AttributeValueDao]

  val sectionDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[SectionDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[SectionDao]

  val folderDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[FolderDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[FolderDao]

  val sf = context.getBean(classOf[SessionFactory])

  val typeDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[TypeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[TypeDao]

  val userDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[UserDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[UserDao]

  val objectDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[ObjectDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[ObjectDao]

  val formDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[FormDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[FormDao]

  val formFieldDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[FormFieldDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[FormFieldDao]

  val listingPackageDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[ListingPackageDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[ListingPackageDao]

  val listingProcessDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[ListingProcessDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[ListingProcessDao]

  val searchFormDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[SearchFormDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[SearchFormDao]

  val dao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[OrderDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[OrderDao]

  val txDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[TransactionDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[TransactionDao]

  val themeDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[ThemeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[ThemeDao]

  val markupDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[MarkupDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[MarkupDao]

  val savedSearchDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[SavedSearchDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[SavedSearchDao]

  val accountPluginDao = context
    .getAutowireCapableBeanFactory
    .createBean(classOf[AccountPluginDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[AccountPluginDao]

  val variationDao = context.getAutowireCapableBeanFactory
    .createBean(classOf[VariationDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[VariationDao]

  val dimensionDao = context.getAutowireCapableBeanFactory
    .createBean(classOf[DimensionDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[DimensionDao]

  val dimensionValueDao = context.getAutowireCapableBeanFactory
    .createBean(classOf[DimensionValueDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
    .asInstanceOf[DimensionValueDao]
}
