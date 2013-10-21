package com.cloudray.scalapress.plugin.listings.controller.renderer

import scala.collection.JavaConverters._
import scala.xml.{Node, Unparsed}
import collection.Iterable
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.obj.attr.{AttributeType, AttributeValue, Attribute}
import com.cloudray.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
object ListingFieldsRenderer {

  def render(process: ListingProcess) = {

    if (process.listingPackage.objectType == null)
      throw new RuntimeException("Object Type is not set on the listing package")

    val attributes = process.listingPackage.objectType.attributes.asScala
    val attributeValues = process.attributeValues.asScala

    <div id="listing-process-details">
    <p>Please fill in as much detail as you can about your listing. The more information you can enter here the more likely your listing is to get noticed.<br /> 
    <em>Please note that fields marked with</em> * <em>are mandatory and must be completed or selected from the available options.</em></p>
      <form method="POST" action="/listing/field">
        <legend>Details</legend>{_title(process.title)}{_genericAttributes(attributes,
        attributeValues)}{_sectionAttributes(
        attributes, attributeValues)}<legend>Main Content</legend>{_content(
        process.content)}<button type="submit" class="btn">Continue</button>
      </form>
    </div>
  }

  def render(obj: Obj) = {

    val attributes = obj.objectType.attributes.asScala
    val attributeValues = obj.attributeValues.asScala

    <div id="listing-edit-fields">
      <form method="POST">
        <legend>Details</legend>{_title(obj.name)}{_genericAttributes(attributes,
        attributeValues)}{_sectionAttributes(
        attributes, attributeValues)}<legend>Main Content</legend>{_content(
        obj.content)}<button type="submit" class="btn btn-primary">Save</button>
      </form>
    </div>
  }

  // show attributes not in a section
  private def _genericAttributes(attributes: Iterable[Attribute], attributeValues: Iterable[AttributeValue]) = {
    val filtered = attributes.filter(attr => attr.section == null || attr.section.trim.length == 0)
      .filter(_.userEditable)
    _attributeInputs(filtered, attributeValues)
  }

  private def _title(title: String) =
    <div>
      <label class="control-label">Title *</label>
      <div class="controls">
        <input type="text" class="input-xlarge" name="title" placeholder="Title" value={title}/>
      </div>
    </div>

  private def _content(content: String) =
    <div>
      <label class="control-label">Content *</label>
      <p>Please enter as much detailed information as you can for the main content section of your listing. There is no limit to the 
      amount of text that you can enter, but please note that you cannot use HTML tags such as <b></b> or any other markup as this is 
      automatically removed.</p>
      <div class="controls">
        <textarea class="input-block-level" rows="12" name="content">
          {content}
        </textarea>
      </div>
    </div>

  def _singleSelection(attr: Attribute) = {
    val name = attr.name + (if (attr.optional) "" else " *")
    <div>
      <label class="control-label">
        {Unparsed(name)}
      </label>
      <div class="controls">
        {_select(attr)}
      </div>
    </div>
  }

  def _select(attr: Attribute): Node = {
    <select name={"attributeValue_" + attr.id} placeholder="Title">
      <option value={""}>Please choose</option>{_options(attr)}
    </select>
  }

  def _options(attr: Attribute): Seq[Node] = {
    attr.options.asScala.map(opt =>
      <option>
        {opt.value}
      </option>)
  }

  def _multipleSelection(attr: Attribute) = {
    val name = attr.name + (if (attr.optional) "" else " *")
    <div>
      <label class="control-label">
        {Unparsed(name)}
      </label>
      <div class="controls">
        {_checkboxes(attr)}
      </div>
    </div>
  }

  def _checkboxes(attr: Attribute): Seq[Node] = {
    val name = "attributeValue_" + attr.id
    attr.options.asScala.map(opt =>
      <label class="checkbox">
        <input type="checkbox" value={opt.value} name={name}/>{opt.value}
      </label>
    )
  }

  def _text(attr: Attribute, size: String, value: Option[String]) = {
    val name = attr.name + (if (attr.optional) "" else " *")
    <div>
      <label class="control-label">
        {Unparsed(name)}
      </label>
      <div class="controls">
        <input type="text" name={"attributeValue_" + attr.id}
               placeholder={attr.placeholder} class={size} value={value.orNull}/>
      </div>
    </div>
  }

  def _yesno(attr: Attribute) =
    <div>
      <label class="control-label">
        {Unparsed(attr.name)}
      </label>
      <div class="controls">
        <label class="checkbox">
          <input type="checkbox" name={"attributeValue_" + attr.id}/>
        </label>
      </div>
    </div>

  def _sectionAttributes(attributes: Iterable[Attribute], attributeValues: Iterable[AttributeValue]) = {

    val filtered = attributes.toSeq
      .filter(attr => Option(attr.section).exists(_.trim.length > 0))
      .filter(_.userEditable)
    val sectioned = filtered.groupBy(_.section)
    sectioned.map(arg => _section(arg._1, arg._2, attributeValues))
  }

  def _section(section: String, attributes: Iterable[Attribute], attributeValues: Iterable[AttributeValue]) = {
    val inputs = _attributeInputs(attributes, attributeValues)
    <div class="attribute-section">
      <legend>
        {section}
      </legend>{inputs}
    </div>
  }

  def _attributeInputs(attributes: Iterable[Attribute], attributeValues: Iterable[AttributeValue]) = {
    attributes.toSeq.sortBy(_.position).map(attr => {
      val value = attributeValues.find(av => av.attribute.id == attr.id).map(_.value)
      attr.attributeType match {
        case AttributeType.Postcode | AttributeType.Numerical => _text(attr, "input-sm", value)
        case AttributeType.Boolean => _yesno(attr)
        case AttributeType.Selection if attr.multipleValues => _multipleSelection(attr)
        case AttributeType.Selection => _singleSelection(attr)
        case _ => _text(attr, "input-xlarge", value)
      }
    })
  }
}
