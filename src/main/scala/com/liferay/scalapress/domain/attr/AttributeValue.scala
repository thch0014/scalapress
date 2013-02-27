package com.liferay.scalapress.domain.attr

import java.lang.String
import javax.persistence.{FetchType, JoinColumn, ManyToOne, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.search.SavedSearch
import org.hibernate.annotations.{BatchSize, FetchMode, Fetch}

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes_values")
class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute", nullable = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 50)
    @BeanProperty var attribute: Attribute = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item", nullable = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var obj: Obj = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search", nullable = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var savedSearch: SavedSearch = _

    @BeanProperty var value: String = _
}
