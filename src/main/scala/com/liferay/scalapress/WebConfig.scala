package com.liferay.scalapress

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.{ResourceHttpMessageConverter, ByteArrayHttpMessageConverter, StringHttpMessageConverter, HttpMessageConverter}
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
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
import util.mvc._
import com.liferay.scalapress.util.mvc.interceptor._
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module
import org.springframework.web.servlet.mvc.WebContentInterceptor
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

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
        registry.addResourceHandler("/static/**").addResourceLocations("/static/**").setCachePeriod(60 * 60 * 24 * 30)
    }

    override def addInterceptors(registry: InterceptorRegistry) {

        registry.addInterceptor(VaryEncodingInterceptor).addPathPatterns("/static/**", "/static")

        val webContentInterceptor = new WebContentInterceptor
        webContentInterceptor.setUseCacheControlHeader(true)
        webContentInterceptor.setCacheSeconds(60 * 60 * 24 * 30)
        registry.addInterceptor(webContentInterceptor).addPathPatterns("/asset/**", "/asset/")

        registry.addInterceptor(SessionInterceptor)
        registry.addInterceptor(UrlResolverInterceptor)
        registry.addInterceptor(new TypesInterceptor(typeDao)).addPathPatterns("/backoffice/**")
        registry.addInterceptor(new SiteInterceptor(siteDao))
        registry.addInterceptor(MenuInterceptor)
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
        mapper.registerModule(new Hibernate3Module)

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

