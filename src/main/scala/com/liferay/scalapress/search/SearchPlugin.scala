package com.liferay.scalapress.search

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Entity, Table}
import reflect.BeanProperty
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_search")
class SearchPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "markup")
    @BeanProperty var markup: Markup = _

    @BeanProperty var noResultsText: String = _
}

trait SearchPluginDao extends GenericDao[SearchPlugin, java.lang.Long] {
    def get: SearchPlugin
}

@Component
@Transactional
class SearchPluginDaoImpl extends GenericDaoImpl[SearchPlugin, java.lang.Long] with SearchPluginDao {
    def get = findAll.head
}

@Component
class SearchPluginDaoValidator {
    @Autowired var searchPluginDao: SearchPluginDao = _
    @PostConstruct def ensureOne() {
        if (searchPluginDao.findAll().size == 0) {
            val plugin = new SearchPlugin
            searchPluginDao.save(plugin)
        }
    }
}
