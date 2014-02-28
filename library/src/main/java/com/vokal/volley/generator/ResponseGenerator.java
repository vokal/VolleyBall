package com.vokal.volley.generator;

import android.content.Context;

import java.util.Map;

import org.apache.http.*;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.BasicHttpContext;

import com.vokal.volley.mock.Route;

public interface ResponseGenerator {

    public static final ProtocolVersion HTTP_1_1 = new ProtocolVersion("Http", 1, 1);
    public static final HttpResponseFactory FACTORY = new DefaultHttpResponseFactory();
    public static final BasicHttpContext CONTEXT = new BasicHttpContext();

    public static final HttpResponse SERVER_ERROR = FACTORY.newHttpResponse(HTTP_1_1, 500, CONTEXT);


    public HttpResponse generateResponse(Context aContext, Route aRoute, Map<String, String> aReplace);
}
