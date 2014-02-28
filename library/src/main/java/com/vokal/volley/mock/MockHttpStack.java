package com.vokal.volley.mock;

import android.content.Context;

import java.util.*;

import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.vokal.volley.generator.*;

import org.apache.http.*;

public class MockHttpStack implements HttpStack {

    public Context mContext;
    public int mRouteRes;
    public List<Route> mRoutes;

    public static final ProtocolVersion HTTP_1_1 = new ProtocolVersion("Http", 1, 1);

    public MockHttpStack(Context aContext, int aRoutes) {
        mContext = aContext.getApplicationContext();
        mRouteRes = aRoutes;
    }

    public HttpResponse performRequest(Request<?> request, Map<String,String> additionalHeaders) {
        if (mRoutes == null) {
            mRoutes = RoutesParser.init(mContext, mRouteRes);
        }

        for (Route r : mRoutes) {
            if (r.matches(request)) {
                return r.generateResponse(mContext, request);
            }
        }

        return ResponseGenerator.FACTORY.newHttpResponse(HTTP_1_1, 404, ResponseGenerator.CONTEXT);
    }
}
