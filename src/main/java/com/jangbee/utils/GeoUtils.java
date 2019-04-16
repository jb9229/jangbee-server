package com.jangbee.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Created by test on 2019-02-16.
 */
public final class GeoUtils {
    public static Geometry wktToGeometry(String wellKnownText) throws ParseException {
        return new WKTReader().read(wellKnownText);
    }

    public static Point getPoint(Double latitude, Double longitude) {
        Geometry geometry;
        try {
            geometry = GeoUtils.wktToGeometry(String.format("POINT (%f %f)", longitude, latitude));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return (Point)geometry;
    }
}
