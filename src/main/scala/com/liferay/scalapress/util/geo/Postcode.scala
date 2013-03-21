package com.liferay.scalapress.util.geo

import org.apache.commons.io.IOUtils
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object Postcode {

    val input = getClass.getResourceAsStream("/postcodes.csv")
    val postcodes = IOUtils.readLines(input).asScala.map(line => {
        val array = line.split(",")
        val postcode = array(0).toUpperCase.replace(" ", "").trim
        val osref = OSRef(array(1).toInt, array(2).toInt)
        (postcode, osref)
    }).toMap

    def osref(postcode: String): Option[OSRef] = _lookup(_postcodeArea(postcode))
    def gps(postcode: String): Option[GPS] = osref(postcode).map(_.toGPS)
    def _lookup(postcode: String): Option[OSRef] = postcodes.get(postcode.toUpperCase)
    def _postcodeArea(postcode: String) = {
        val trimmed = postcode.replaceAll(" ", "").trim
        if (trimmed.length > 4) trimmed.dropRight(3) else trimmed
    }
}

abstract class Coordinate {
    def toOSRef: OSRef
    def toGPS: GPS
    def distance(other: Coordinate): Double = {
        val R = 6371
        val gps1 = other.toGPS
        val gps2 = other.toGPS
        val x = (gps2.lon - gps1.lon) * scala.math.cos((gps1.lat + gps2.lat) / 2)
        val y = (gps2.lat - gps1.lat)
        val d = scala.math.sqrt(x * x + y * y) * R
        d
    }
}

case class OSRef(easting: Int, northing: Int) extends Coordinate {

    def toOSRef: OSRef = this
    def toGPS = {

        val a = 6377563.396
        val b = 6356256.910

        val F0 = 0.9996012717
        val lat0 = 49 * scala.math.Pi / 180
        val lon0 = -2 * scala.math.Pi / 180

        val N0 = -100000
        val E0 = 400000

        val e2 = 1 - (b * b) / (a * a)
        val n = (a - b) / (a + b)
        val n2 = n * n
        val n3 = n * n * n

        var lat = lat0
        var M = 0.0
        do {
            lat = (northing - N0 - M) / (a * F0) + lat

            val Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (lat - lat0)
            val Mb = (3 * n + 3 * n * n + (21 / 8) * n3) * scala.math.sin(lat - lat0) * scala.math.cos(lat + lat0)
            val Mc = ((15 / 8) * n2 + (15 / 8) * n3) * scala.math.sin(2 * (lat - lat0)) * scala
              .math
              .cos(2 * (lat + lat0))
            val Md = (35 / 24) * n3 * scala.math.sin(3 * (lat - lat0)) * scala.math.cos(3 * (lat + lat0))
            M = b * F0 * (Ma - Mb + Mc - Md)

        } while (northing - N0 - M >= 0.00001)

        val cosLat = scala.math.cos(lat)
        val sinLat = scala.math.sin(lat)
        val nu = a * F0 / scala.math.sqrt(1 - e2 * sinLat * sinLat)
        val rho = a * F0 * (1 - e2) / scala.math.pow(1 - e2 * sinLat * sinLat, 1.5)
        val eta2 = nu / rho - 1

        val tanLat = scala.math.tan(lat)
        val tan2lat = scala.math.pow(tanLat, 2)
        val tan4lat = scala.math.pow(tanLat, 4)
        val tan6lat = scala.math.pow(tanLat, 6)
        val secLat = 1 / cosLat
        val nu3 = scala.math.pow(nu, 3)
        val nu5 = scala.math.pow(nu, 5)
        val nu7 = scala.math.pow(nu, 7)
        val VII = tanLat / (2 * rho * nu)
        val VIII = tanLat / (24 * rho * nu3) * (5 + 3 * tan2lat + eta2 - 9 * tan2lat * eta2)
        val IX = tanLat / (720 * rho * nu5) * (61 + 90 * tan2lat + 45 * tan4lat)
        val X = secLat / nu
        val XI = secLat / (6 * nu3) * (nu / rho + 2 * tan2lat)
        val XII = secLat / (120 * nu5) * (5 + 28 * tan2lat + 24 * tan4lat)
        val XIIA = secLat / (5040 * nu7) * (61 + 662 * tan2lat + 1320 * tan4lat + 720 * tan6lat)

        val de = (easting - E0)
        lat = lat - VII * scala.math.pow(de, 2) + VIII * scala.math.pow(de, 4) - IX * scala.math.pow(de, 6)
        val lon = lon0 + X * de - XI * scala.math.pow(de, 3) + XII * scala.math.pow(de, 5) - XIIA * scala
          .math
          .pow(de, 7)

        new GPS(scala.math.toDegrees(lat), scala.math.toDegrees(lon))
    }
}

case class GPS(lat: Double, lon: Double) extends Coordinate {
    def string() = lat + "," + lon
    def toOSRef = null
    def toGPS = this
}