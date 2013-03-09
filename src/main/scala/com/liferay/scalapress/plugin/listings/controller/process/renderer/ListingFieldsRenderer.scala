package com.liferay.scalapress.plugin.listings.controller.process.renderer

import com.liferay.scalapress.plugin.listings.ListingProcess
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.AttributeType
import com.liferay.scalapress.domain.attr.{AttributeValue, Attribute}
import xml.Unparsed
import com.liferay.scalapress.domain.Obj
import collection.Iterable

/** @author Stephen Samuel */
object ListingFieldsRenderer {

    def render(process: ListingProcess) = {

        if (process.listingPackage.objectType == null)
            throw new RuntimeException("Object Type is not set on the listing package")

        val attributes = process.listingPackage.objectType.attributes.asScala
        val attributeValues = process.attributeValues.asScala

        <div id="listing-process-details">
            <form method="POST">
                <legend>Details</legend>{_title(process.title)}{_genericAttributes(attributes,
                attributeValues)}{_attributes(
                attributes, attributeValues)}<legend>Main Content</legend>{_content(
                process.content)}<button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>
    }

    def render(obj: Obj) = {

        val attributes = obj.objectType.attributes.asScala
        val attributeValues = obj.attributeValues.asScala

        <div id="listing-edit-fields">
            <form method="POST">
                <legend>Details</legend>{_title(obj.name)}{_genericAttributes(attributes, attributeValues)}{_attributes(
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
            <div class="controls">
                <textarea class="input-block-level" rows="12" name="content" value={content}/>
            </div>
        </div>

    private def _options(attr: Attribute) =
        attr.options.asScala.map(opt =>
            <option>
                {opt.value}
            </option>)

    private def _renderSelection(attr: Attribute) =
        <div>
            <label class="control-label">
                {Unparsed(attr.name)}
            </label>
            <div class="controls">
                <select name={"attributeValue_" + attr.id} placeholder="Title">
                    {_options(attr)}
                </select>
            </div>
        </div>

    def _renderText(attr: Attribute, size: String, value: Option[String]) =
        <div>
            <label class="control-label">
                {Unparsed(attr.name)}
            </label>
            <div class="controls">
                <input type="text" name={"attributeValue_" + attr.id}
                       placeholder={attr.placeholder} class={size} value={value.orNull}/>
            </div>
        </div>

    def _renderYesNo(attr: Attribute) =
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

    def _attributes(attributes: Iterable[Attribute], attributeValues: Iterable[AttributeValue]) = {

        val filtered = attributes.toSeq
          .filterNot(attr => attr.section == null || attr.section.trim.length > 0)
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
                case AttributeType.Postcode | AttributeType.Numerical => _renderText(attr, "input-small", value)
                case AttributeType.Boolean => _renderYesNo(attr)
                case AttributeType.Selection => _renderSelection(attr)
                case _ => _renderText(attr, "input-xlarge", value)
            }
        })
    }
}
