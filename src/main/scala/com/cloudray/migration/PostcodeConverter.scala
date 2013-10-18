package com.cloudray.migration

import java.io.{FileWriter, FileReader, File}
import org.apache.commons.io.IOUtils
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object PostcodeConverter extends App {

  val dir = "c:/development/postcodes"
  val output = "c:/development/postcodes.csv"

  val map = new java.util.TreeMap[String, String]()
  new File(dir).listFiles().foreach(arg => {
    IOUtils.readLines(new FileReader(arg)).asScala.foreach(line => {
      val fields = line.split(",")
      val postcode = fields(0).replace("\"", "").replace(" ", "").dropRight(3)
      //      map.put(postcode.dropRight(1), postcode.dropRight(1) + "," + fields(2) + "," + fields(3))
      map.put(postcode, postcode + "," + fields(2) + "," + fields(3))
    })
  })

  val writer = new FileWriter(output)
  IOUtils.write(map.values().asScala.mkString("\n"), writer)
  writer.flush()
  writer.close()
}
