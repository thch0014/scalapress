package com.cloudray.scalapress.search

import javax.persistence._
import com.cloudray.scalapress.enums.Sort
import com.cloudray.scalapress.obj.ObjectType
import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms")
class SearchForm {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: java.lang.Long = _

  @BeanProperty var name: String = _

  @ManyToOne
  @JoinColumn(name = "itemtype", nullable = true)
  @BeanProperty var objectType: ObjectType = _

  @Column(name = "resultsperpage")
  @BeanProperty var pageSize: Int = _

  @Enumerated(EnumType.STRING)
  @BeanProperty var sort: Sort = _

  @BeanProperty var submitLabel: String = _

  @OneToMany(mappedBy = "searchForm", cascade = Array(CascadeType.ALL))
  @BeanProperty var fields: java.util.Set[SearchFormField] = new java.util.HashSet[SearchFormField]()

  def sortedFields = fields.asScala.toSeq.sortBy(_.name).sortBy(_.position)

}
