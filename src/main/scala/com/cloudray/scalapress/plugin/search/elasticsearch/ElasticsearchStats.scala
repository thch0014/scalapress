package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.search.SearchStats
import com.sksamuel.elastic4s.ElasticClient
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
trait ElasticsearchStats extends ElasticsearchUtils with SearchStats {

  val client: ElasticClient

  def stats: Map[String, String] = {
    val nodes = client.admin.cluster().prepareNodesStats().all().execute().actionGet(TIMEOUT).getNodes
    val map = scala.collection.mutable.Map[String, String]()
    nodes.flatMap(node => {
      map.put("jvm.mem.heapUsed", node.getJvm.mem.heapUsed.mb + "mb")
      map.put("jvm.mem.heapCommitted", node.getJvm.mem.heapCommitted.mb + "mb")
      map
        .put("jvm.mem.nonHeapCommitted",
        node.getJvm.mem.nonHeapCommitted.mb + "mb")
      map.put("jvm.mem.nonHeapUsed", node.getJvm.mem.nonHeapUsed.mb + "mb")
      map.put("jvm.threads.count", node.getJvm.threads.count.toString)
      map.put("jvm.threads.peakCount", node.getJvm.threads.peakCount.toString)

      map.put("indices.docs.count", node.getIndices.getDocs.getCount.toString)
      map
        .put("indices.docs.deleted",
        node.getIndices.getDocs.getDeleted.toString)

      map.put("indices.store.size", node.getIndices.getStore.size.mb + "mb")
      map
        .put("indices.store.throttleTime",
        node.getIndices.getStore.throttleTime.toString)

      map.put("transport.txCount", node.getTransport.txCount.toString)
      map.put("transport.txSize", node.getTransport.txSize.toString)
      map.put("transport.txSize", node.getTransport.txSize.toString)

      node.getThreadPool.iterator().asScala.foreach(pool => {
        map.put("threadpool." + pool.getName + ".active", pool.getActive.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getCompleted.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getLargest.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getQueue.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getRejected.toString)
        map.put("threadpool." + pool.getName + ".active", pool.getThreads.toString)
      })

      var k = 1
      node.getFs.iterator().asScala.foreach(fs => {
        map.put("threadpool.fs" + k + ".available", fs.getAvailable.toString)
        map.put("threadpool.fs" + k + ".diskQueue", fs.getDiskQueue.toString)
        map.put("threadpool.fs" + k + ".diskReads", fs.getDiskReads.toString)
        map.put("threadpool.fs" + k + ".diskWrites", fs.getDiskWrites.toString)
        map.put("threadpool.fs" + k + ".free", fs.getFree.toString)
        map.put("threadpool.fs" + k + ".total", fs.getTotal.toString)
        k = k + 1
      })

      map.map(arg => ("search." + node.getHostname + "." + arg._1, arg._2))

    }).toMap
  }

}
