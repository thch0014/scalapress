package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.AttributeType
import com.liferay.scalapress.domain.attr.Attribute
import xml.Unparsed

/** @author Stephen Samuel */
object ListingDetailsRenderer {

    def render(process: ListingProcess) =
        <div id="listing-process-details">
            <form method="POST">
                {_title(process)}{_attributes(process)}<button type="submit">Continue</button>
            </form>
        </div>

    def _title(process: ListingProcess) =
        <div>
            <label class="control-label">Title</label>
            <div class="controls">
                <input type="text" class="input-xlarge" name="title" placeholder="Title" value={process.title}/>
            </div>
        </div>

    def _options(attr: Attribute) =
        attr.options.asScala.map(opt =>
            <option>
                {opt.value}
            </option>)

    def _renderSelection(attr: Attribute) =
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

    def _renderText(attr: Attribute, size: String) =
        <div>
            <label class="control-label">
                {Unparsed(attr.name)}
            </label>
            <div class="controls">
                <input type="text" name={"attributeValue_" + attr.id} placeholder={attr.placeholder} class={size}/>
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
        process.listingPackage.objectType.attributes.asScala.map(attr => {
            attr.attributeType match {
                case AttributeType.Postcode | AttributeType.Numerical => _renderText(attr, "input-small")
                case AttributeType.Boolean => _renderYesNo(attr)
                case AttributeType.Selection => _renderSelection(attr)
                case _ => _renderText(attr, "input-xlarge")
            }
        })
    }
}
