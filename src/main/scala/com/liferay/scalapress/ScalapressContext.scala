package com.liferay.scalapress

import dao.settings.SiteDao
import dao.{AttributeDao, FormDao, SubmissionDao, GalleryDao, MarkupDao, FolderDao, WidgetDao, ObjectDao, TypeDao}
import service.asset.AssetStore
import service.folder.FolderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.ServletContextAware
import javax.servlet.ServletContext

/** @author Stephen Samuel */
@Component
class ScalapressContext extends ServletContextAware {

    @Autowired var siteDao: SiteDao = _
    @Autowired var attributeDao: AttributeDao = _
    @Autowired var widgetDao: WidgetDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var folderService: FolderService = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var assetStore: AssetStore = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var galleryDao: GalleryDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var formDao: FormDao = _

    var servletContext: ServletContext = _
    def setServletContext(servletContext: ServletContext) {
        this.servletContext = servletContext
    }
}
