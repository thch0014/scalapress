package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.widgets.Widget
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */

trait WidgetDao extends GenericDao[Widget, java.lang.Long]

@Component
@Transactional
class WidgetDaoImpl extends GenericDaoImpl[Widget, java.lang.Long] with WidgetDao