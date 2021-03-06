package com.cloudray.scalapress.search

import section.SearchFormSection
import scala.xml.{Utility, Node, Unparsed, Elem}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search.widget.SearchFormWidget
import com.cloudray.scalapress.item.attr.AttributeType

/** @author Stephen Samuel */
object SearchFormRenderer {

  def render(form: SearchForm, widget: SearchFormWidget): String = {
    _render(form, "widgetId", widget.id.toString)
  }

  def render(form: SearchForm, section: SearchFormSection): String = {
    _render(form, "sectionId", section.id.toString)
  }

  def _render(form: SearchForm, sourceParam: String, sourceValue: String): String = {

    val sort = Option(form.sort).getOrElse(Sort.Name)

    val fields = form.sortedFields
    val renderedFields = renderFields(fields)

    val submit = Option(form.submitLabel).getOrElse("Submit")
    val objectType = Option(form.objectType)
      .map(objectType => <input type="hidden" value={objectType.id.toString} name="type"/>).orNull

    <form method="GET" action="/search">
      <input type="hidden" name="sort" value={sort.name}/>
      <input type="hidden" name={sourceParam} value={sourceValue}/>{objectType}{renderedFields}<button type="submit">
      {submit}
    </button>
    </form>.toString()
  }

  private def renderFields(fields: Seq[SearchFormField]): Seq[Node] = {
    fields.map(field => {
      field.fieldType match {
        case SearchFieldType.Attribute if field.preset => _renderPresetAttributeField(field)
        case SearchFieldType.Categories if field.preset => _renderPresetField("folder", field.value)
        case SearchFieldType.Name if field.preset => _renderPresetField("name", field.value)
        case SearchFieldType.Attribute => renderAttributeField(field)
        case SearchFieldType.Distance => _renderDistanceField(field)
        case SearchFieldType.Location => _renderLocationField(field)
        case _ => _renderTextField("name", field.name)
      }
    })
  }

  def _renderDateField(field: SearchFormField): Node = {
    scala.xml.Utility.trim(<div>
      <label>
        {Unparsed(field.name)}
      </label>
      <input type="text" name={field.id.toString} class="input-medium datepicker"/>
    </div>)
  }

  def _renderLocationField(field: SearchFormField): Node = {
    scala.xml.Utility.trim(<div>
      <label>
        {Unparsed(field.name)}
      </label>
      <input type="text" name="location" class="input-medium"/>
    </div>)
  }

  def _renderDistanceField(field: SearchFormField): Node = {
    scala.xml.Utility.trim(<div>
      <label>
        {Unparsed(field.name)}
      </label>
      <select name="distance">
        <option value="1">
          1 mile
        </option>
        <option value="5">
          5 miles
        </option>
        <option value="10">
          10 miles
        </option>
        <option value="25" selected="true">
          25 miles
        </option>
        <option value="50">
          50 miles
        </option>
        <option value="100">
          100 miles
        </option>
      </select>
    </div>)
  }

  def _renderPresetAttributeField(field: SearchFormField): Elem = {
    val name = "attr_" + Option(field.attribute).map(_.id).getOrElse("~")
      <input type="hidden" name={name} value={field.value}/>
  }

  def _renderPresetField(name: String, value: String): Elem = {
      <input type="hidden" name={name} value={value}/>
  }

  private def renderAttributeField(field: SearchFormField): Node = {
    val attributeType = Option(field.attribute).flatMap(attr => Option(attr.attributeType))
    attributeType match {
      case Some(AttributeType.Selection) => renderSelectionAttribute(field)
      case Some(AttributeType.Date) => _renderDateField(field)
      case _ => _renderTextAttribute(field)
    }
  }

  def _renderOptions(field: SearchFormField) = {
    field.attribute.orderedOptions.asScala.map(opt =>
      scala.xml.Utility.trim(<option value={opt.value}>
        {opt.value}
      </option>))
  }

  private def renderSelectionAttribute(field: SearchFormField): Node = {
    val renderedOptions = _renderOptions(field)
    val name = "attr_" + Option(field.attribute).map(_.id).orNull
    _renderSelection(name, field.name, renderedOptions)
  }

  def _renderSelection(name: String, label: String, options: Iterable[Node]): Node = {
    val any = ""
    val xml = <div>
      <label>
        {Unparsed(label)}
      </label>
      <select name={name}>
        <option value={any}>
          Any
        </option>{options}
      </select>
    </div>
    Utility.trim(xml)
  }

  def _renderTextAttribute(field: SearchFormField): Node = {
    val name = "attr_" + Option(field.attribute).map(_.id).orNull
    _renderTextField(name, field.name)
  }

  def _renderTextField(name: String, label: String): Node = {
    scala.xml.Utility.trim(<div>
      <label>
        {Unparsed(label)}
      </label>
      <input type="text" name={name}/>
    </div>)
  }
}
