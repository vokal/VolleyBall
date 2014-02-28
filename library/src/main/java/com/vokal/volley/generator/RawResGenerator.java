package com.vokal.volley.generator;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.Map;

import org.apache.http.*;
import org.apache.http.entity.InputStreamEntity;

import com.vokal.volley.mock.Route;

public class RawResGenerator implements ResponseGenerator {

    public String mText;
     
    public RawResGenerator(String aResponse) {
        mText = aResponse;
    }

    public HttpResponse generateResponse(Context aContext, Route aRoute, Map<String, String> aReplace) {
        try {
            Resources res = aContext.getResources();
            String[] asset = mText.split(".");

            int id = res.getIdentifier(asset[2], asset[1], null);
            InputStream is = res.openRawResource(id);

            HttpResponse response = FACTORY.newHttpResponse(HTTP_1_1, 200, CONTEXT);
            response.setEntity(new InputStreamEntity(is, is.available()));
            return response;
        } catch (Exception e) {
            return SERVER_ERROR;
        }
    }
}
