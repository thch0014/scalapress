package com.cloudray.scalapress.widgets

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */

trait WidgetDao extends GenericDao[Widget, java.lang.Long]

@Component
@Transactional
class WidgetDaoImpl extends GenericDaoImpl[Widget, java.lang.Long] with WidgetDao