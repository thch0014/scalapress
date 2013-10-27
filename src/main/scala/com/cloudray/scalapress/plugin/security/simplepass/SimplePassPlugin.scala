package com.cloudray.scalapress.plugin.security.simplepass

import javax.persistence._
import scala.beans.BeanProperty
import org.hibernate.annotations._
import java.util
import com.cloudray.scalapress.folder.Folder
import javax.persistence.Table
import javax.persistence.CascadeType
import javax.persistence.Entity
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_security_simplepass")
class SimplePassPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var username: String = _

  @BeanProperty
  var password: String = _

  @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JoinTable(name = "plugin_security_simplepass_folders",
    joinColumns = Array(new JoinColumn(name = "plugin")),
    inverseJoinColumns = Array(new JoinColumn(name = "folder"))
  )
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var folders: java.util.Set[Folder] = new util.HashSet[Folder]()
}

trait SimplePassPluginDao extends GenericDao[SimplePassPlugin, java.lang.Long] {
  def get: SimplePassPlugin
}

@Component
@Transactional
class SimplePassPluginDaoImpl extends GenericDaoImpl[SimplePassPlugin, java.lang.Long] with SimplePassPluginDao {
  def get: SimplePassPlugin = findAll.head
}

@Component
@Autowired
class SimplePassPluginValidator(dao: SimplePassPluginDao) {
  @PostConstruct def ensureOne(): Unit = if (dao.findAll().size == 0) dao.save(new SimplePassPlugin)
}

