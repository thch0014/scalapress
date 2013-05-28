package com.cloudray.scalapress.plugin.feed.rightmove

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/** @author Stephen Samuel */
@JsonIgnoreProperties(ignoreUnknown = true)
case class Property(id: String,
                    price: Int,
                    bedrooms: Int,
                    address: String,
                    latitude: Double,
                    longitude: Double,
                    propertyType: String,
                    firstVisible: String,
                    image: String,
                    status: String)

@JsonIgnoreProperties(ignoreUnknown = true)
case class SearchResults(locationName: String, property: List[Property], totalResults: Int)
