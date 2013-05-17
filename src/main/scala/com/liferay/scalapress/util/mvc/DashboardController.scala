package com.liferay.scalapress.util.mvc

import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.search.{ObjectRef, SavedSearch}
import com.liferay.scalapress.enums.Sort
import scala.collection.JavaConverters._
import com.liferay.scalapress.security.SecurityFuncs
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice"))
class DashboardController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def dashboard = "admin/dashboard.vm"

    @ModelAttribute("installation") def installation = context.installationDao.get
    @ModelAttribute("indexed") def indexed = context.searchService.count
    @ModelAttribute("user") def indexed(req: HttpServletRequest) = SecurityFuncs.getAdminDetails(req).user
    @ModelAttribute("folderCount") def folderCount = context.folderDao.findAll().size
    @ModelAttribute("recentObjects") def recentObjects: java.util.List[ObjectRef] = {
        val search = new SavedSearch
        search.sortType = Sort.Newest
        search.maxResults = 8
        val refs = context.searchService.search(search)
        refs.asJava
    }
}
