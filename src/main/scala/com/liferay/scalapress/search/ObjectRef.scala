package com.liferay.scalapress.search

/** @author Stephen Samuel */
case class ObjectRef(id: Long,
                     objectType: Long,
                     name: String,
                     status: String,
                     attributes: Map[Long, String],
                     folders: Seq[Long])
