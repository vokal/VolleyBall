package com.vokal.volley.mock;

import android.content.Context;
import org.xmlpull.v1.XmlPullParser;

import java.util.*;

public class RoutesParser {

    public static List<Route> init(Context aContext, int aRes) {
        try {
            return parseRoutes(aContext.getResources().getXml(aRes));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Route>(0);
        }
    }

    private static List<Route> parseRoutes(XmlPullParser aXpp) throws Exception {
        // Will begin at a START_DOCUMENT event type
        boolean inRoutes = false;
        int eventType = aXpp.getEventType();
        List<Route> routes = new ArrayList<Route>(25);
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (aXpp.getName().equals("routes")) {
                    inRoutes = true;
                } else if (inRoutes && aXpp.getName().equals("route")) {
                    routes.add(parse(aXpp));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (aXpp.getName().equals("routes")) {
                    inRoutes = false;
                }
            }
            eventType = aXpp.next();
        }
        return routes;
    }

    private static Route parse(XmlPullParser aXpp) throws Exception {
        // Will begin at a START_TAG event type
        int eventType = aXpp.getEventType();
        if (eventType != XmlPullParser.START_TAG) {
            throw new RuntimeException("Not at correct parse position in routes file");
        }

        Route.Builder builder = new Route.Builder();

        String type = aXpp.getAttributeValue(null, "type");
        if (type == null) type = "get";

        builder.type(type);

        String path = aXpp.getAttributeValue(null, "path");
        if (path == null) {
            throw new RuntimeException("route tag must contain a path");
        }

        builder.route(path);

        eventType = aXpp.next();
        if (eventType != XmlPullParser.TEXT) {
            throw new RuntimeException("Not at correct parse position in routes file");
        }
        String value = aXpp.getText();

        builder.response(value);
        // Should verify aParser.next() event is END_TAG

        eventType = aXpp.next();
        if (eventType != XmlPullParser.END_TAG) {
            throw new RuntimeException("Not at correct parse position in routes file");
        }
        return builder.build();
    }
}
