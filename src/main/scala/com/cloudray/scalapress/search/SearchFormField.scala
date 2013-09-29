package com.cloudray.scalapress.search

import javax.persistence.{EnumType, Enumerated, JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import SearchFieldType
import org.hibernate.annotations.{FetchMode, Fetch}
import com.cloudray.scalapress.obj.attr.Attribute
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms_fields")
class SearchFormField {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: java.lang.Long = _

  @Column(name = "type")
  @Enumerated(value = EnumType.STRING)
  @BeanProperty var fieldType: SearchFieldType = _

  @Column(name = "name")
  @BeanProperty var name: String = _

  @Column(name = "hidden")
  @BeanProperty var preset: Boolean = _

  @Column(name = "value")
  @BeanProperty var value: String = _

  @Column(name = "position")
  @BeanProperty var position: Int = _

  @ManyToOne
  @JoinColumn(name = "searchForm")
  @Fetch(FetchMode.JOIN)
  @BeanProperty var searchForm: SearchForm = _

  @ManyToOne
  @JoinColumn(name = "attribute")
  @Fetch(FetchMode.JOIN)
  @BeanProperty var attribute: Attribute = _

}
