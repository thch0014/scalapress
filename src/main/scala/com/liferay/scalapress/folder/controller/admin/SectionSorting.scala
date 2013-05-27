package com.liferay.scalapress.folder.controller.admin

import com.liferay.scalapress.section.{Section, SectionDao}

/** @author Stephen Samuel */
trait SectionSorting {

    var sectionDao: SectionDao

    def reorderSections(order: String, sections: Iterable[Section]) = {
        val ids = order.split("-")
        if (ids.size == sections.size)
            sections.foreach(section => {
                val pos = ids.indexOf(section.id.toString)
                section.position = pos
                sectionDao.save(section)
            })
        "ok"
    }
}
