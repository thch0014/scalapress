package com.cloudray.scalapress.search.section

/** @author Stephen Samuel */
trait SingleObjectCache[T] {

  val CacheTimeout: Long
  var _result: Option[T] = None
  var _cacheTime: Long = 0

  /**
   * Returns the current cached value if any, or if there is no cached value or the cache timeout has been
   * reached, then the supplied function will be invoked and the value of that used as the result.
   */
  def cachedOrExecute(f: => T): T =
    if (_cacheTime < System.currentTimeMillis() - CacheTimeout) _execute(f)
    else _result.getOrElse(_execute(f))

  def _execute(f: => T): T = {
    val t = f
    _result = Option(t)
    _cacheTime = System.currentTimeMillis()
    t
  }
}
