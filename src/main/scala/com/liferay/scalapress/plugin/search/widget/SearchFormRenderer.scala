package com.liferay.scalapress.plugin.search.widget

import com.liferay.scalapress.plugin.search.form.{SearchFormField, SearchForm}
import xml.Elem
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object SearchFormRenderer {

    def renderForm(form: SearchForm): String = {
        val fields = renderFields(form.fields.asScala)
        <form method="GET" action="/search">
            {fields}<button type="submit">Submit</button>
        </form>.toString
    }

    def renderFields(fields: Seq[SearchFormField]): Seq[Elem] = {
        fields.map(field => {
            <div>
                <label>
                    {field.name}
                </label>
                <input type="text" name={field.id.toString}/>
            </div>
        })
    }
}
