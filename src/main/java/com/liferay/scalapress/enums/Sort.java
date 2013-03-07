package com.liferay.scalapress.enums;

public enum Sort {
    // sort from the location set by the search
    Distance,
    Name,
    Newest,
    Oldest,
    // lowest price first
    Price,
    // highest price first
    PriceHigh,

    // sort all objects randomly
    Random,

    Attribute
}