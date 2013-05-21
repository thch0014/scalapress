package com.liferay.scalapress.settings

import java.lang.management.ManagementFactory

/** @author Stephen Samuel */
object Debug {

    val mxbean = ManagementFactory.getRuntimeMXBean

    def vmVender = mxbean.getVmVendor
    def vmName = mxbean.getVmName
    def uptime = mxbean.getUptime

    def processors = Runtime.getRuntime.availableProcessors()
    def freeMemory = Runtime.getRuntime.freeMemory()
    def maxMemory = Runtime.getRuntime.maxMemory()
    def totalMemory = Runtime.getRuntime.totalMemory()

    def map: Map[String, String] = {
        Map("jvm.processors" -> processors.toString,
            "jvm.freeMemory" -> freeMemory.toString,
            "jvm.maxMemory" -> maxMemory.toString,
            "jvm.totalMemory" -> totalMemory.toString,
            "jvm.vmVendor" -> vmVender,
            "jvm.vmName" -> vmName,
            "jvm.uptime" -> uptime.toString)
    }

}
