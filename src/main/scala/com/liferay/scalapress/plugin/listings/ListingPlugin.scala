package com.liferay.scalapress.plugin.listings

import javax.persistence._
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.settings.lifecycle.{MenuItem, MenuLifecycle}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_listings")
class ListingsPlugin extends MenuLifecycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    //@Column(columnDefinition = "double default 0")
    def vatRate: Double = 0

    @BeanProperty var packagesPageText: String = _
    @BeanProperty var foldersPageText: String = _
    @BeanProperty var imagesPageText: String = _

    def menu: Option[MenuItem] =
        Some(MenuItem("Plugins", MenuItem("Listing Packages", "icon-th-large", "/backoffice/plugin/listings")))
}

trait ListingsPluginDao extends GenericDao[ListingsPlugin, java.lang.Long] {
    def get: ListingsPlugin
}

@Component
@Transactional
class ListingsPluginDaoImpl extends GenericDaoImpl[ListingsPlugin, java.lang.Long] with ListingsPluginDao {
    def get = findAll.head
}

@Component
class ListingsPluginDaoValidator {
    @Autowired var dao: ListingsPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new ListingsPlugin
            dao.save(plugin)
        }
    }
}