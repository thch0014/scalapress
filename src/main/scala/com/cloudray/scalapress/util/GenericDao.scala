package com.cloudray.scalapress.util

import com.googlecode.genericdao.search.ISearch

/** @author Stephen Samuel */
trait GenericDao[T <: AnyRef, ID <: java.io.Serializable] {

  /**
   * <p>
   * Get the entity with the specified type and id from the datastore.
   *
   * <p>
   * If none is found, return null.
   */
  def find(id: ID): T

  /**
   * <p>
   * If the id of the entity is null or zero, add it to the datastore and
   * assign it an id; otherwise, update the corresponding entity in the
   * datastore with the properties of this entity. In either case the entity
   * passed to this method will be attached to the session.
   *
   * <p>
   * If an entity to update is already attached to the session, this method
   * will have no effect. If an entity to update has the same id as another
   * instance already attached to the session, an error will be thrown.
   *
   * @return <code>true</code> if create; <code>false</code> if update.
   */
  def save(entity: T): Boolean

  def save(entities: Iterable[T])

  /**
   * Remove the specified entity from the datastore.
   *
   * @return <code>true</code> if the entity is found in the datastore and
   *         removed, <code>false</code> if it is not found.
   */
  def remove(entity: T): Boolean

  def remove(entities: Iterable[T])

  /**
   * Remove the entity with the specified type and id from the datastore.
   *
   * @return <code>true</code> if the entity is found in the datastore and
   *         removed, <code>false</code> if it is not found.
   */
  def removeById(id: ID): Boolean

  /**
   * Get a list of all the objects of the specified type.
   */
  def findAll: List[T]
  def findAll(klass: Class[T]): List[T]
  def findAll(limit: Int): List[T]

  /**
   * Search for entities given the search parameters in the specified
   * <code>ISearch</code> obj.
   *
   * x
   */
  def search(search: ISearch): List[T]

  def update(entity: T)
  def merge(entity: T)

  /**
   * Search for a single entity using the given parameters.
   *
   * The result type is automatically determined by the context in which the method is called.
   */
  def searchUnique(search: ISearch): T

  /**
   * Returns the total number of results that would be returned using the
   * given <code>ISearch</code> if there were no paging or maxResults limits.
   */
  def count(search: ISearch): Int

  /**
   * Returns a <code>SearchResult</code> obj that includes both the list of
   * results like <code>search()</code> and the total length like
   * <code>count()</code>.
   *
   * The result type is automatically determined by the context in which the method is called.
   */
  //    def searchAndCount[RT](search: ISearch): SearchResult[RT]

}
