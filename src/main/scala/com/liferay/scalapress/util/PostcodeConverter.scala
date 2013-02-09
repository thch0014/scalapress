package com.liferay.scalapress.util

import java.io.{FileWriter, FileReader, File}
import org.apache.commons.io.IOUtils
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object PostcodeConverter extends App {

    val dir = "/home/sam/development/postcodes"
    val output = "/home/sam/development/postcodes.csv"

    val map = new java.util.TreeMap[String, String]()
    new File(dir).listFiles().foreach(arg => {
        IOUtils.readLines(new FileReader(arg)).asScala.foreach(line => {
            val fields = line.split(",")
            val postcode = fields(0).replace("\"", "").replace(" ", "").dropRight(1)
            map.put(postcode, postcode + "," + fields(2) + "," + fields(3))
        })
    })

    IOUtils.write(map.values().asScala.mkString("\n"), new FileWriter(output))
}
