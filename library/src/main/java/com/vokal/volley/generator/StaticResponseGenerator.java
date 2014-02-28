package com.vokal.volley.generator;

import android.content.Context;

import java.util.Map;

import org.apache.http.*;

import com.vokal.volley.mock.Route;

public class StaticResponseGenerator implements ResponseGenerator {

    public String mText;
     
    public StaticResponseGenerator(String aResponse) {
        mText = aResponse;
    }

    public HttpResponse generateResponse(Context aContext, Route aRoute, Map<String, String> aReplace) {
        try {
            Integer code = Integer.parseInt(mText);
            HttpResponse response = FACTORY.newHttpResponse(HTTP_1_1, code, CONTEXT);
            return response;
        } catch (Exception e) {
            return SERVER_ERROR;
        }
    }
}
