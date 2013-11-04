package com.cloudray.scalapress.plugin.search.typeahead

import com.cloudray.scalapress.theme.tag.ScalapressTag
import java.util.UUID
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("typeahead")
class TypeaheadTag extends ScalapressTag {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

        val id = params.get("id").getOrElse(UUID.randomUUID().toString)
        // val limit = params.get("limit").getOrElse("8")
        //val url = "/typeahead?q=%QUERY" + params.get("objectType").map("&objectTypeId=" + _).getOrElse("")

        val script =
            """<script>
                    $('#""" + id + """').typeahead({
                        minLength: 2,
                        source: function (query, process) {
                            return $.ajax({
                                url: '/typeahead',
                                type: 'GET',
                                data: {q: query},
                                dataType: 'json',
                                success: function (json) {
                                      process(json);
                                }
                            });
                        },
                        updater: function(item) {
                            window.location = '/search?q=' + item;
                            return item;
                        }
                    })
                </script>"""

        val form = <form method="GET" action="/search">
            <input type="input" name="q" id={id} autocomplete="off" data-provide="typeahead"/>
        </form>

        Some(form.toString + "\n" + script)
    }
}
