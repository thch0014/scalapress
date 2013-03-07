package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.AttributeType
import com.liferay.scalapress.domain.attr.Attribute
import xml.Unparsed

/** @author Stephen Samuel */
object ListingFieldsRenderer {

    def render(process: ListingProcess) =
        <div id="listing-process-details">
            <form method="POST">
                <legend>Details</legend>{_title(process)}{_email(process)}<legend>Main Content</legend>{_content(process)}<legend>Specific Details</legend>{_attributes(
                process)}<button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>

    private def _title(process: ListingProcess) =
        <div>
            <label class="control-label">Title</label>
            <div class="controls">
                <input type="text" class="input-xlarge" name="title" placeholder="Title" value={process.title}/>
            </div>
        </div>

    private def _email(process: ListingProcess) =
        <div>
            <label class="control-label">Email</label>
            <div class="controls">
                <input type="text" class="input-xlarge" name="email" placeholder="Your email" value={process.email}/>
            </div>
        </div>

    private def _content(process: ListingProcess) =
        <div>
            <label class="control-label">Content</label>
            <div class="controls">
                <textarea class="input-block-xlarge" name="content" value={process.content}/>
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

        if (process.listingPackage.objectType == null)
            throw new RuntimeException("Object Type is not set on the listing package")

        val attributes = process.listingPackage.objectType.attributes.asScala.toSeq
        val sorted = attributes.sortBy(_.position)
        sorted.map(attr => {

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
