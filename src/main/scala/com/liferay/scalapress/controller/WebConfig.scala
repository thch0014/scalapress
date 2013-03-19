package com.liferay.scalapress.controller

import admin.interceptor.{SiteInterceptor, TypesInterceptor, UrlResolverInterceptor}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.{ResourceHttpMessageConverter, ByteArrayHttpMessageConverter, StringHttpMessageConverter, HttpMessageConverter}
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import web.interceptor.SessionInterceptor
import web.{ScalapressPageRenderer, ScalaPressPageMessageConverter}
import com.liferay.scalapress.{StringToAttributeConvertor, StringFormConverter, StringToAddressConverter, StringToThemeConverter, StringDeliveryOptionConverter, StringSearchFormConverter, StringMarkupConverter, StringObjectTypeConverter, StringFolderConverter, ScalapressContext}
import com.liferay.scalapress.plugin.ecommerce.dao.{AddressDao, DeliveryOptionDao, BasketDao}
import com.liferay.scalapress.plugin.form.FormDao
import com.liferay.scalapress.search.SearchFormDao
import org.springframework.http.converter.xml.{SourceHttpMessageConverter, XmlAwareFormHttpMessageConverter}
import java.nio.charset.Charset
import javax.xml.transform.Source
import com.liferay.scalapress.folder.FolderDao
import com.liferay.scalapress.obj.TypeDao
import com.liferay.scalapress.theme.{MarkupDao, ThemeDao}
import com.liferay.scalapress.settings.InstallationDao

/**
 * @author Stephen K Samuel 14 Oct 2012
 */
@Configuration
class WebConfig extends WebMvcConfigurationSupport {

    @Autowired var context: ScalapressContext = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var themeDao: ThemeDao = _
    @Autowired var addressDao: AddressDao = _
    @Autowired var searchFormDao: SearchFormDao = _
    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var siteDao: InstallationDao = _
    @Autowired var formDao: FormDao = _

    override def addFormatters(registry: FormatterRegistry) {
        registry.addConverter(new StringFolderConverter(folderDao))
        registry.addConverter(new StringObjectTypeConverter(typeDao))
        registry.addConverter(new StringMarkupConverter(markupDao))
        registry.addConverter(new StringSearchFormConverter(searchFormDao))
        registry.addConverter(new StringDeliveryOptionConverter(deliveryOptionDao))
        registry.addConverter(new StringToThemeConverter(themeDao))
        registry.addConverter(new StringToAddressConverter(addressDao))
        registry.addConverter(new StringFormConverter(formDao))
        registry.addConverter(new StringToAttributeConvertor(context.attributeDao))
    }

    override def addResourceHandlers(registry: ResourceHandlerRegistry) {

    }

    override def addInterceptors(registry: InterceptorRegistry) {

        //        val webContentInterceptor = new WebContentInterceptor
        //        webContentInterceptor.setCacheSeconds(60)
        //        webContentInterceptor.setUseExpiresHeader(true)
        //        webContentInterceptor.setUseCacheControlHeader(true)
        //        webContentInterceptor.setUseCacheControlNoStore(true)
        //
        //        registry.addInterceptor(webContentInterceptor).addPathPatterns("/static/**")
        registry.addInterceptor(SessionInterceptor)
        registry.addInterceptor(UrlResolverInterceptor)
        registry.addInterceptor(new TypesInterceptor(typeDao)).addPathPatterns("/backoffice/**")
        registry.addInterceptor(new SiteInterceptor(siteDao))
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


