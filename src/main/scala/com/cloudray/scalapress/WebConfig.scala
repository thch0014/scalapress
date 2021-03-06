package com.cloudray.scalapress

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.{ResourceHttpMessageConverter, ByteArrayHttpMessageConverter, StringHttpMessageConverter, HttpMessageConverter}
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import com.cloudray.scalapress.plugin.form.FormDao
import com.cloudray.scalapress.search.SearchFormDao
import org.springframework.http.converter.xml.{SourceHttpMessageConverter, XmlAwareFormHttpMessageConverter}
import java.nio.charset.Charset
import javax.xml.transform.Source
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.item.ItemTypeDao
import com.cloudray.scalapress.theme.{MarkupDao, ThemeDao}
import com.cloudray.scalapress.settings.{GeneralSettingsDao, InstallationDao}
import util.mvc._
import com.cloudray.scalapress.util.mvc.interceptor._
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.mvc.WebContentInterceptor
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.cloudray.scalapress.account.AccountTypeDao
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.gallery.base.GalleryDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{DeliveryOptionDao, AddressDao, BasketDao}

/**
 * @author Stephen K Samuel 14 Oct 2012
 */
@Configuration
class WebConfig extends WebMvcConfigurationSupport {

  @Autowired var context: ScalapressContext = _
  @Autowired var folderDao: FolderDao = _
  @Autowired var typeDao: ItemTypeDao = _
  @Autowired var accountTypeDao: AccountTypeDao = _
  @Autowired var basketDao: BasketDao = _
  @Autowired var markupDao: MarkupDao = _
  @Autowired var themeDao: ThemeDao = _
  @Autowired var addressDao: AddressDao = _
  @Autowired var searchFormDao: SearchFormDao = _
  @Autowired var deliveryOptionDao: DeliveryOptionDao = _
  @Autowired var siteDao: InstallationDao = _
  @Autowired var formDao: FormDao = _
  @Autowired var galleryDao: GalleryDao = _
  @Autowired var generalSettingsDao: GeneralSettingsDao = _
  @Autowired var installationDao: InstallationDao = _

  override def addFormatters(registry: FormatterRegistry) {
    registry.addConverter(new StringFolderConverter(folderDao))
    registry.addConverter(new StringItemTypeConverter(typeDao))
    registry.addConverter(new StringMarkupConverter(markupDao))
    registry.addConverter(new StringSearchFormConverter(searchFormDao))
    registry.addConverter(new StringDeliveryOptionConverter(deliveryOptionDao))
    registry.addConverter(new StringToThemeConverter(themeDao))
    registry.addConverter(new StringToAddressConverter(addressDao))
    registry.addConverter(new StringFormConverter(formDao))
    registry.addConverter(new StringToAttributeConvertor(context.attributeDao))
    registry.addConverter(new StringToGalleryConverter(galleryDao))
  }

  override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/static/**").addResourceLocations("/static/**").setCachePeriod(60 * 60 * 24 * 30)
  }

  override def addInterceptors(registry: InterceptorRegistry) {

    val webContentInterceptor = new WebContentInterceptor
    webContentInterceptor.setUseCacheControlHeader(true)
    webContentInterceptor.setCacheSeconds(60 * 60 * 24 * 30)
    registry.addInterceptor(webContentInterceptor).addPathPatterns("/asset/**", "/asset/")

    registry.addInterceptor(SessionInterceptor)
    registry.addInterceptor(new ItemTypesInterceptor(typeDao)).addPathPatterns("/backoffice/**")
    registry.addInterceptor(new AccountTypesInterceptor(accountTypeDao)).addPathPatterns("/backoffice/**")
    registry.addInterceptor(new SiteInterceptor(context))
    registry.addInterceptor(new MenuInterceptor(context)).addPathPatterns("/backoffice/**")
    registry.addInterceptor(new OfflineInterceptor(generalSettingsDao, installationDao))
  }

  override def configureMessageConverters(converters: java.util.List[HttpMessageConverter[_]]) {

    converters.add(new ScalaPressPageMessageConverter(new ScalapressPageRenderer(context)))
    converters.add(new ByteArrayHttpMessageConverter)
    val stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"))
    stringConverter.setWriteAcceptCharset(false)
    converters.add(stringConverter)
    converters.add(new ResourceHttpMessageConverter)
    converters.add(new SourceHttpMessageConverter[Source])
    converters.add(new XmlAwareFormHttpMessageConverter)

    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.registerModule(new Hibernate4Module)

    val convertor = new MappingJackson2HttpMessageConverter
    convertor.setObjectMapper(mapper)
    converters.add(convertor)
  }

  override def requestMappingHandlerAdapter: RequestMappingHandlerAdapter = {
    val adapter = super.requestMappingHandlerAdapter
    adapter.setIgnoreDefaultModelOnRedirect(true)
    adapter
  }

  override def requestMappingHandlerMapping: RequestMappingHandlerMapping = {
    val rm = super.requestMappingHandlerMapping
    rm.setUseSuffixPatternMatch(false)
    rm.setUseTrailingSlashMatch(true)
    rm
  }

}

object VaryEncodingInterceptor extends HandlerInterceptorAdapter {
  override def afterCompletion(request: HttpServletRequest,
                               response: HttpServletResponse,
                               handler: Any,
                               ex: Exception) {
    response.setHeader("Vary", "Accept-Encoding")
  }
}

