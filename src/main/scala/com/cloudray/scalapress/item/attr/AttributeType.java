package com.cloudray.scalapress.item.attr;

import com.cloudray.scalapress.util.geo.Postcode;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import java.awt.*;

public enum AttributeType {
    Association,
    Boolean,
    Date,
    DateTime,
    Email,
    Image,
    Link,
    Numerical,
    Postcode,
    Selection,
    Text,
    TextArea
}