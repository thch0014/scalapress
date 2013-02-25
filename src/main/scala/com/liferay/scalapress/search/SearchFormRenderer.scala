package com.liferay.scalapress.search

import xml.Elem
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
object SearchFormRenderer {

    def render(form: SearchForm): String = {
        val fields = renderFields(form.fields.asScala)
        val submit = Option(form.submitLabel).getOrElse("Submit")
        <form method="GET" action="/search">
            {fields}<button type="submit">
            {submit}
        </button>
        </form>.toString()
    }

    private def renderFields(fields: Seq[SearchFormField]): Seq[Elem] = {
        fields.map(field => {
            field.fieldType match {
                case SearchFieldType.Attribute => renderAttributeField(field)
                case _ => renderTextField(field)
            }
        })
    }

    private def renderAttributeField(field: SearchFormField): Elem =
        field.attribute.attributeType match {
            case AttributeType.Selection => renderSelectionAttribute(field)
            case _ => renderTextField(field)
        }

    private def renderSelectionAttribute(field: SearchFormField): Elem =
        <div>
            <label>
                {field.name}
            </label>
            <select type="text" name={field.id.toString}>
                <option>Any</option>
            </select>
        </div>

    private def renderTextField(field: SearchFormField): Elem =
        <div>
            <label>
                {field.name}
            </label>
            <input type="text" name={field.id.toString}/>
        </div>
}
