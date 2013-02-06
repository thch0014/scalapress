package com.liferay.scalapress.enums;

public enum Sort {
    Name,
    Newest,
    Oldest,
    // lowest price first
    Price,
    // highest price first
    PriceHigh,
    // sort from the location set by the search
    Distance,
    // sort all objects randomly
    Random
}