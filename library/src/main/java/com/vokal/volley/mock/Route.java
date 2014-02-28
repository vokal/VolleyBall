package com.vokal.volley.mock;

import android.content.Context;

import com.android.volley.Request;

import org.apache.http.*;

import java.util.*;

import com.vokal.volley.generator.*;

public class Route {

    public static final int GET    = Request.Method.GET;
    public static final int POST   = Request.Method.POST;
    public static final int PUT    = Request.Method.PUT;
    public static final int DELETE = Request.Method.DELETE;

    public Map<String, String> params = new HashMap<String, String>();
    public int type;
    public String route;
    public ResponseGenerator response;

    public boolean matches(Request<?> aRequest) {
        // Check if method is the same
        if (aRequest.getMethod() != type) return false;

        String url = aRequest.getUrl();

        // Check if parameters are required for this request
        String[] parts = url.split("[?]");
        if (parts.length == 1 && params.size() > 0) return false;

        String base = parts[0];

        // Check Path
        if (!base.endsWith(route)) return false;

        if (params.size() > 0) {
            String[] reqParams = parts[1].split("&");
            int count = 0;
            for(String p : reqParams) {
                String[] keyval = p.split("=");
                if (params.containsKey(keyval[0])) {
                    count++;
                    String val = params.get(keyval[0]);
                    if (!val.startsWith("{") && !val.endsWith("}")) {
                        // Check each Static parameter
                        if (!val.equals(keyval[1])) return false;
                    }
                }
            }

            // Validate that all parameters were fulfilled
            if (count != params.size()) return false;
        }

        return true;
    }

    public Map<String, String> getReplacements(Request aRequest) {
        Map<String, String> results = new HashMap<String, String>();
        String url = aRequest.getUrl();
        String[] parts = url.split("[?]");
        if (params.size() > 0) {
            String[] reqParams = parts[1].split("&");
            for(String p : reqParams) {
                String[] keyval = p.split("=");
                if (params.containsKey(keyval[0])) {
                    String val = params.get(keyval[0]);
                    if (val.startsWith("{") && val.endsWith("}")) {
                        results.put(val, keyval[1]);
                    }
                }
            }
        }
        return results;
    }

    public HttpResponse generateResponse(Context aContext, Request<?> aRequest) {
        if (response != null) {
            Map<String, String> replacements = getReplacements(aRequest);
            return response.generateResponse(aContext, this, replacements);
        }
        return ResponseGenerator.SERVER_ERROR;
    }

    /** route types.
     *      all parameters will match unless specified explicitly
     *      options are exact value or parameterized for response
     *      <route 
     *          type="get"
     *          path="dir/dir/endpoint?user={token}&page={page}"
     *          >
     *          mock/something/{token}.{page}.json
     *      </route>
     *      
     *      other non-parameterized options include
     *
     *      <route>
     *          R.raw.file
     *      </route>
     *
     *      <route>
     *          R.string.string
     *      </route>
     *
     *      or
     *
     *      <route>
     *         200 <!-- Http Response Code with no body --> 
     *      </route>
     */

    static class Builder {
        Route mRoute = new Route();

        public Builder type(String aType) {
            if ("get".equals(aType)) {
                mRoute.type = GET;
            } else if ("post".equals(aType)) {
                mRoute.type = POST;
            } else if ("put".equals(aType)) {
                mRoute.type = PUT;
            } else {
                mRoute.type = DELETE;
            }
            return this;
        }

        public Builder route(String route) {
            if (route.contains("?")) {
                String[] pieces = route.split("[?]");
                mRoute.route = pieces[0];

                if (pieces.length > 1) {
                    mRoute.params = parseParams(pieces[1]);
                }
            } else {
                mRoute.route = route;
            }
            return this;
        }

        private Map<String, String> parseParams(String aParams) {
            HashMap<String, String> result = new HashMap<String, String>();
            String[] params = aParams.split("&");
            for (String param : params) {
                String[] keyval = param.split("=");
                result.put(keyval[0], keyval[1]);
            }
            return result;
        }

        public Builder response(String aText) {
            if (aText.contains("R.raw")) {
                mRoute.response = new RawResGenerator(aText);
            } else if (aText.contains("R.string")) {
                mRoute.response = new StringResGenerator(aText);
            } else if (aText.length() == 3) {
                mRoute.response = new StaticResponseGenerator(aText); 
            } else {
                mRoute.response = new AssetGenerator(aText);
            }
            return this;
        }

        public Route build() {
            return mRoute;
        }
    }
}
