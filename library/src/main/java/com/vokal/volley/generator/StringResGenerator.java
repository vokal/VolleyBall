package com.vokal.volley.generator;

import android.content.Context;
import android.content.res.Resources;

import java.util.Map;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;

import com.vokal.volley.mock.Route;

public class StringResGenerator implements ResponseGenerator {

    public String mText;
     
    public StringResGenerator(String aResponse) {
        mText = aResponse;
    }

    public HttpResponse generateResponse(Context aContext, Route aRoute, Map<String, String> aReplace) {

        try {
            Resources res = aContext.getResources();
            String[] asset = mText.split(".");

            int id = res.getIdentifier(asset[2], asset[1], null);
            String val = aContext.getString(id);

            HttpResponse response = FACTORY.newHttpResponse(HTTP_1_1, 200, CONTEXT);
            response.setEntity(new StringEntity(val));
            return response;
        } catch (Exception e) {
            return SERVER_ERROR;
        }
    }
}
