package com.liferay.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.attr.{AttributeOption, Attribute}
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class SearchFormRendererTest extends FunSuite with MockitoSugar {

    val option1 = new AttributeOption
    option1.value = "coldplay"

    val option2 = new AttributeOption
    option2.value = "keane"

    val field = new SearchFormField
    field.name = "super field"
    field.id = 14
    field.attribute = new Attribute
    field.attribute.id = 2
    field.attribute.attributeType = AttributeType.Selection
    field.attribute.options.add(option1)
    field.attribute.options.add(option2)

    test("selection render happy path") {
        val rendered = SearchFormRenderer
          ._renderSelection("selfield", "my sel field", SearchFormRenderer._renderOptions(field))
        assert(scala.xml.Utility.trim(<div>
            <label>my sel field</label> <select name="selfield">
                <option value=" ">Any</option> <option value="coldplay">coldplay</option> <option value="keane">keane</option>
            </select>
        </div>) === rendered)
    }

    test("selection options render happy path") {
        val rendered = SearchFormRenderer._renderOptions(field)
        assert(List(scala.xml.Utility.trim(<option value="coldplay">coldplay</option>),
            scala.xml.Utility.trim(<option value="keane">keane</option>)) === rendered.toList)
    }

    test("text field render happy path") {
        val rendered = SearchFormRenderer._renderTextField("textfield", "my field")
        assert(scala.xml.Utility.trim(<div>
            <label>my field</label> <input name="textfield" type="text"></input>
        </div>) === rendered)
    }

    test("text attribute field render happy path") {
        val rendered = SearchFormRenderer._renderTextAttribute(field)
        assert(scala.xml.Utility.trim(<div>
            <label>super field</label> <input name="attr_2" type="text"></input>
        </div>) === rendered)
    }

    test("date field render happy path") {
        val rendered = SearchFormRenderer._renderDateField(field)
        assert(scala.xml.Utility.trim(<div>
            <label>super field</label> <input name="14" type="text" class="input-medium datepicker"></input>
        </div>) === rendered)
    }

    test("location render happy path") {
        val rendered = SearchFormRenderer._renderLocationField(field)
        assert(scala.xml.Utility.trim(<div>
            <label>super field</label> <input name="location" type="text" class="input-medium"></input>
        </div>)
          === rendered)
    }

    test("preset render happy path") {
        val rendered = SearchFormRenderer._renderPresetField("hiddenfield", "super value")
        assert(scala
          .xml
          .Utility
          .trim(<input name="hiddenfield" type="hidden" value="super value"></input>) === rendered)
    }

    test("distance render happy path") {
        val rendered = SearchFormRenderer._renderDistanceField(field)
        assert(scala.xml.Utility.trim(<div>
            <label>super field</label>
            <select name="distance">
                <option value="1">1 mile</option>
                <option value="5">5 miles</option>
                <option value="10">10 miles</option>
                <option selected="true" value="25">25 miles</option>
                <option value="50">50 miles</option>
                <option value="100">100 miles</option>
            </select>
        </div>) === rendered)
    }
}
