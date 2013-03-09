package com.liferay.scalapress.controller

import admin.interceptor.UrlResolverInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.WebContentInterceptor
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import web.interceptor.SessionInterceptor
import web.{ScalapressPageRenderer, ScalaPressPageMessageConverter}
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.{TypeDao, FolderDao}
import org.springframework.core.convert.converter.Converter
import com.liferay.scalapress.domain.{ObjectType, Folder}

/**
 * @author Stephen K Samuel 14 Oct 2012
 */
@Configuration
class WebConfig extends WebMvcConfigurationSupport {

    @Autowired var scalapressContext: ScalapressContext = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var typeDao: TypeDao = _

    override def addFormatters(registry: FormatterRegistry) {
        registry.addConverter(new StringFolderConverter(folderDao))
        registry.addConverter(new StringObjectTypeConverter(typeDao))

    }

    override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    }

    override def addInterceptors(registry: InterceptorRegistry) {

        val webContentInterceptor = new WebContentInterceptor
        webContentInterceptor.setCacheSeconds(0)
        webContentInterceptor.setUseExpiresHeader(true)
        webContentInterceptor.setUseCacheControlHeader(true)
        webContentInterceptor.setUseCacheControlNoStore(true)

        registry.addInterceptor(webContentInterceptor)
        registry.addInterceptor(SessionInterceptor)
        registry.addInterceptor(new UrlResolverInterceptor)
    }

    override def configureMessageConverters(converters: java.util.List[HttpMessageConverter[_]]) {
        super.addDefaultHttpMessageConverters(converters)
        converters.add(0, new ScalaPressPageMessageConverter(new ScalapressPageRenderer(scalapressContext)))
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

class StringFolderConverter(folderDao: FolderDao) extends Converter[String, Folder] {
    def convert(source: String): Folder = if (source == null || source == "") null else folderDao.find(source.toInt)
}

class StringObjectTypeConverter(objectTypeDao: TypeDao) extends Converter[String, ObjectType] {
    def convert(source: String): ObjectType = if (source == null || source == "") null
    else objectTypeDao.find(source.toInt)
}