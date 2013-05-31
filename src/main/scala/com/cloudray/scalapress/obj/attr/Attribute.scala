package com.cloudray.scalapress.obj.attr

import javax.persistence._
import org.hibernate.validator.constraints.NotEmpty
import java.util
import scala.Array
import com.cloudray.scalapress.enums.AttributeType
import org.hibernate.annotations.Index
import com.cloudray.scalapress.obj.ObjectType
import collection.mutable
import scala.collection.JavaConverters._
import scala.beans.BeanProperty

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

    def optionsAsMap: java.util.Map[String, String] = {
        val map = new mutable.LinkedHashMap[String, String]()
        val sorted = manualOptionsOrdering match {
            case true => options.asScala.sortBy(_.position)
            case _ => options.asScala.sortBy(_.value)
        }
        sorted.foreach(opt => map.put(opt.value, opt.value))
        map.asJava
    }

    def orderedOptions: java.util.List[AttributeOption] = manualOptionsOrdering match {
        case true => options.asScala.sortBy(a => Option(a.position).getOrElse(0)).asJava
        case false => options.asScala.sorted.asJava
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @javax.validation.constraints.NotNull
    @BeanProperty var attributeType: AttributeType = _

    @Column(name = "customoptionsorder", nullable = false)
    @BeanProperty var manualOptionsOrdering: Boolean = _

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
