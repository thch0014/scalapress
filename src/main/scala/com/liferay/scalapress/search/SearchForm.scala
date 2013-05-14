package com.liferay.scalapress.search

import javax.persistence._
import org.hibernate.annotations.{CacheConcurrencyStrategy, Fetch, FetchMode}
import com.liferay.scalapress.enums.Sort
import com.liferay.scalapress.obj.ObjectType
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "search_forms")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class SearchForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: java.lang.Long = _

    @BeanProperty var name: String = _

    @ManyToOne
    @JoinColumn(name = "itemtype")
    @BeanProperty var objectType: ObjectType = _

    @Column(name = "resultsperpage")
    @BeanProperty var pageSize: Int = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var sort: Sort = _

    @BeanProperty var submitLabel: String = _

    @OneToMany(mappedBy = "searchForm", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.JOIN)
    @BeanProperty var fields: java.util.Set[SearchFormField] = new java.util.HashSet[SearchFormField]()

}
