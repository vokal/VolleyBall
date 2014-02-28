package com.vokal.volley.generator;

import android.content.Context;

import java.io.InputStream;
import java.util.Map;

import org.apache.http.*;
import org.apache.http.entity.InputStreamEntity;

import com.vokal.volley.mock.Route;

public class AssetGenerator implements ResponseGenerator {

    public String mText;
     
    public AssetGenerator(String aResponse) {
        mText = aResponse;
    }

    public HttpResponse generateResponse(Context aContext, Route aRoute, Map<String, String> aReplace) {
        try {
            String test = mText;
            for (String aKey : aReplace.keySet()) {
                android.util.Log.d("AssetGenerator", "Replacing: "+ aKey);
                String regex = "\\{" + aKey.substring(1, aKey.length() - 1) + "\\}";
                test= test.replaceAll(regex, aReplace.get(aKey));
            }

            InputStream is = aContext.getAssets().open(test);
            HttpResponse response = FACTORY.newHttpResponse(HTTP_1_1, 200, CONTEXT);
            response.setEntity(new InputStreamEntity(is, is.available()));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return SERVER_ERROR;
        }
    }
}
