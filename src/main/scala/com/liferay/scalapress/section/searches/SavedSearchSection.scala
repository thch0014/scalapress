package com.liferay.scalapress.section.searches

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
class SavedSearchSection extends Section {

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] =
        Some("savedsearches")
    def desc: String = ""
}
