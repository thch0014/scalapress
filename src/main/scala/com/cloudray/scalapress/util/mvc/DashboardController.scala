package com.cloudray.scalapress.util.mvc

import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.search.{ObjectRef, SavedSearch}
import com.cloudray.scalapress.enums.Sort
import scala.collection.JavaConverters._
import com.cloudray.scalapress.security.SpringSecurityResolver
import javax.servlet.http.HttpServletRequest
import java.util.Properties
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.obj.ObjectQuery

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice"))
class DashboardController {

    val in = getClass.getResourceAsStream("/buildNumber.properties")
    val props = new Properties()
    props.load(in)
    IOUtils.closeQuietly(in)

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def dashboard = "admin/dashboard.vm"

    @ModelAttribute("installation") def installation = context.installationDao.get
    @ModelAttribute("indexed") def indexed = context.searchService.count
    @ModelAttribute("user") def indexed(req: HttpServletRequest) = SpringSecurityResolver.getAdminDetails(req).user
    @ModelAttribute("folderCount") def folderCount = context.folderDao.findAll().size

    @ModelAttribute("recentObjects") def recentObjects: java.util.List[ObjectRef] = {
        context.objectDao.recent(8)

        val search = new SavedSearch
        search.sortType = Sort.Newest
        search.maxResults = 8
        val result = context.searchService.search(search)
        result.refs.asJava
    }

    @ModelAttribute("buildNumber") def buildNumber = props.get("buildNumber")
}
