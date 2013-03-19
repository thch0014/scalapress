package com.liferay.scalapress.obj.attr

import javax.persistence._
import reflect.BeanProperty
import org.hibernate.validator.constraints.NotEmpty
import java.util
import scala.Array
import com.liferay.scalapress.enums.AttributeType
import org.hibernate.annotations.Index
import com.liferay.scalapress.obj.ObjectType

/** @author Stephen Samuel */
@Entity
@Table(name = "attributes")
class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @NotEmpty
    @BeanProperty var name: String = _

    @ManyToOne
    @JoinColumn(name = "itemType", nullable = true)
    @Index(name = "objecttype_index")
    @BeanProperty var objectType: ObjectType = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var options: java.util.List[AttributeOption] = new util.ArrayList[AttributeOption]()

    import scala.collection.JavaConverters._

    def optionsAsMap: java.util.Map[String, String] = options
      .asScala
      .sortBy(_.value)
      .map(opt => (opt.value, opt.value))
      .toMap
      .asJava

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @javax.validation.constraints.NotNull
    @BeanProperty var attributeType: AttributeType = _

    @BeanProperty var description: String = _

    @BeanProperty var section: String = _

    @Column(name = "facet", nullable = false)
    @BeanProperty var facet: Boolean = _

    @Column(name = "displayable", nullable = false)
    @BeanProperty var public: Boolean = _

    @BeanProperty var placeholder: String = _

    @Column(name = "editable")
    @BeanProperty var userEditable: Boolean = _

    @BeanProperty var optional: Boolean = _

    @Column(name = "multi")
    @BeanProperty var multipleValues: Boolean = _

    @BeanProperty var position: Int = _

    @BeanProperty var prefix: String = _

    @BeanProperty var suffix: String = _

}
