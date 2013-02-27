package com.liferay.scalapress.search

import xml.Elem
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.SearchFieldType
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
object SearchFormRenderer {

    def render(form: SearchForm): String = {
        val fields = renderFields(form.fields.asScala.toSeq)
        val submit = Option(form.submitLabel).getOrElse("Submit")
        val objectType = Option(form.objectType)
          .filter(_ > 0)
          .map(id => <input type="hidden" value={id.toString} name="type"/>).orNull

        <form method="GET" action="/search">
            {objectType}{fields}<button type="submit">
            {submit}
        </button>
        </form>.toString()
    }

    private def renderFields(fields: Seq[SearchFormField]): Seq[Elem] = {
        fields.map(field => {
            field.fieldType match {
                case SearchFieldType.Attribute if field.preset => renderPresetAttributeField(field)
                case SearchFieldType.Attribute => renderAttributeField(field)
                case _ => renderTextField(field)
            }
        }
        )
    }

    private def renderPresetAttributeField(field: SearchFormField) = {
        val name = "attr_" + field.attribute.id
        val value = field.value
            <input type="hidden" name={name} value={value}/>
    }

    private def renderAttributeField(field: SearchFormField): Elem =
        field.attribute.attributeType match {
            case AttributeType.Selection => renderSelectionAttribute(field)
            case _ => renderTextField(field)
        }

    private def renderSelectionAttribute(field: SearchFormField): Elem = {
        val options = field.attribute.options.asScala.map(opt =>
            <option value={opt.value}>
                {opt.value}
            </option>)

        val name = "attr_" + field.attribute.id

        <div>
            <label>
                {field.name}
            </label>
            <select type="text" name={name}>
                <option value=" ">
                    Any
                </option>{options}
            </select>
        </div>
    }

    private def renderTextAttribute(field: SearchFormField): Elem = {
        val name = "attr_" + field.attribute.id
        <div>
            <label>
                {field.name}
            </label>
            <input type="text" name={name}/>
        </div>
    }

    private def renderTextField(field: SearchFormField): Elem =
        <div>
            <label>
                {field.name}
            </label>
            <input type="text" name={field.id.toString}/>
        </div>
}
