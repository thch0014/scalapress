package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.AttributeType
import com.liferay.scalapress.domain.attr.Attribute
import xml.Unparsed

/** @author Stephen Samuel */
object ListingFieldsRenderer {

    def render(process: ListingProcess) = {

        if (process.listingPackage.objectType == null)
            throw new RuntimeException("Object Type is not set on the listing package")

        <div id="listing-process-details">
            <form method="POST">
                <legend>Details</legend>{_title(process)}{_genericAttributes(process)}{_attributes(
                process)}<legend>Main Content</legend>{_content(
                process)}<button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>
    }

    // show attributes not in a section
    private def _genericAttributes(process: ListingProcess) = {
        val attributes = process
          .listingPackage
          .objectType
          .attributes
          .asScala.toSeq
          .filter(attr => attr.section == null || attr.section.trim.length == 0)
          .filter(_.userEditable)
          .sortBy(_.position)
        _attributeInputs(attributes, process)
    }

    private def _title(process: ListingProcess) =
        <div>
            <label class="control-label">Title *</label>
            <div class="controls">
                <input type="text" class="input-xlarge" name="title" placeholder="Title" value={process.title}/>
            </div>
        </div>

    private def _content(process: ListingProcess) =
        <div>
            <label class="control-label">Content *</label>
            <div class="controls">
                <textarea class="input-block-level" rows="12" name="content" value={process.content}/>
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

    def _attributes(process: ListingProcess) = {

        val attributes = process
          .listingPackage
          .objectType
          .attributes
          .asScala
          .toSeq
          .filterNot(attr => attr.section == null || attr.section.trim.length > 0)
          .filter(_.userEditable)
        val sectioned = attributes.groupBy(_.section)
        sectioned.map(arg => _section(arg._1, arg._2, process))
    }

    def _section(section: String, attributes: Seq[Attribute], process: ListingProcess) = {
        val inputs = _attributeInputs(attributes.sortBy(_.position), process)
        <div class="attribute-section">
            <legend>
                {section}
            </legend>{inputs}
        </div>
    }

    def _attributeInputs(attributes: Seq[Attribute], process: ListingProcess) = {
        attributes.map(attr => {
            val value = process.attributeValues.asScala.find(av => av.attribute.id == attr.id).map(_.value)
            attr.attributeType match {
                case AttributeType.Postcode | AttributeType.Numerical => _renderText(attr, "input-small", value)
                case AttributeType.Boolean => _renderYesNo(attr)
                case AttributeType.Selection => _renderSelection(attr)
                case _ => _renderText(attr, "input-xlarge", value)
            }
        })
    }
}
