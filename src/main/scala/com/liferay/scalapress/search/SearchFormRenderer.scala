package com.liferay.scalapress.search

import section.SearchFormSection
import xml.{Unparsed, Elem}
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.{Sort, SearchFieldType, AttributeType}

/** @author Stephen Samuel */
object SearchFormRenderer {

    def render(form: SearchForm, section: SearchFormSection): String = {

        val sort = Option(form.sort).getOrElse(Sort.Name)
        val fields = form.fields.asScala.toSeq.sortBy(_.position)
        val renderedFields = renderFields(fields)

        val submit = Option(form.submitLabel).getOrElse("Submit")
        val objectType = Option(form.objectType)
          .map(objectType => <input type="hidden" value={objectType.id.toString} name="type"/>).orNull

        <form method="GET" action="/search">
            <input type="hidden" name="sort" value={sort.name}/>
            <input type="hidden" name="sectionId" value={section
          .id
          .toString}/>{objectType}{renderedFields}<button type="submit">
            {submit}
        </button>
        </form>.toString()
    }

    private def renderFields(fields: Seq[SearchFormField]): Seq[Elem] = {
        fields.map(field => {
            field.fieldType match {
                case SearchFieldType.Attribute if field.preset => renderPresetAttributeField(field)
                case SearchFieldType.Categories if field.preset => _renderPresetField("folder", field.value)
                case SearchFieldType.Name if field.preset => _renderPresetField("name", field.value)
                case SearchFieldType.Attribute => renderAttributeField(field)
                case SearchFieldType.Distance => _renderDistanceField(field)
                case SearchFieldType.Location => _renderLocationField(field)
                case _ => renderTextField(field)
            }
        })
    }

    def _renderDateField(field: SearchFormField) = {
        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <input type="text" name={field.id.toString} class="input-medium datepicker"/>
        </div>
    }

    def _renderLocationField(field: SearchFormField) = {
        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <input type="text" name="location" class="input-medium"/>
        </div>
    }

    def _renderDistanceField(field: SearchFormField) = {
        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <select name="distance">
                <option value="1">1 mile</option>
                <option value="5">5 miles</option>
                <option value="10">10 miles</option>
                <option value="25" selected="true">25 miles</option>
                <option value="50">50 miles</option>
                <option value="100">100 miles</option>
            </select>
        </div>
    }

    private def renderPresetAttributeField(field: SearchFormField): Elem = {
        val name = "attr_" + Option(field.attribute).map(_.id).getOrElse("~")
            <input type="hidden" name={name} value={field.value}/>
    }

    def _renderPresetField(name: String, value: String): Elem = {
            <input type="hidden" name={name} value={value}/>
    }

    private def renderAttributeField(field: SearchFormField): Elem = {
        val attributeType = Option(field.attribute).flatMap(attr => Option(attr.attributeType))
        attributeType match {
            case Some(AttributeType.Selection) => renderSelectionAttribute(field)
            case Some(AttributeType.Date) => _renderDateField(field)
            case _ => renderTextAttribute(field)
        }
    }

    private def renderSelectionAttribute(field: SearchFormField): Elem = {
        val options = field.attribute.options.asScala.map(opt =>
            <option value={opt.value}>
                {opt.value}
            </option>)

        val name = "attr_" + field.attribute.id

        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <select name={name}>
                <option value=" ">
                    Any
                </option>{options}
            </select>
        </div>
    }

    private def renderTextAttribute(field: SearchFormField): Elem = {
        val name = "attr_" + Option(field.attribute).map(_.id).orNull
        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <input type="text" name={name}/>
        </div>
    }

    private def renderTextField(field: SearchFormField): Elem =
        <div>
            <label>
                {Unparsed(field.name)}
            </label>
            <input type="text" name={field.id.toString}/>
        </div>
}
