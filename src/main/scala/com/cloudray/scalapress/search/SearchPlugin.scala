package com.cloudray.scalapress.search

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Entity, Table}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.cloudray.scalapress.theme.Markup
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.SingleInstance

/** @author Stephen Samuel */
@Entity
@SingleInstance
@Table(name = "plugins_search")
class SearchPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToOne
  @JoinColumn(name = "markup")
  @BeanProperty
  var markup: Markup = _

  @BeanProperty
  var noResultsText: String = _
}

trait SearchPluginDao extends GenericDao[SearchPlugin, java.lang.Long] {
  def get: SearchPlugin
}

@Component
@Transactional
class SearchPluginDaoImpl extends GenericDaoImpl[SearchPlugin, java.lang.Long] with SearchPluginDao {
  def get = findAll.head
}
