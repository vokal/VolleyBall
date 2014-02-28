package com.test;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import dagger.*;
import barstool.BarstoolModule;

import com.vokal.volley.VolleyBall;

public class TestApp extends Application {

    public ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyBall module = new VolleyBall(this);
        module.addServer("Production", "http://www.google.com/");
        module.addServer("Staging", "http://staging.google.com/");
        module.addMock(R.xml.routes);

        mObjectGraph = ObjectGraph.create(new DummyModule(), module);
        
    }

    public ObjectGraph graph() {
        return mObjectGraph;
    }

    @Module(
        complete=false,
        injects=MainActivity.class,
        includes=BarstoolModule.class
    )
    public static class DummyModule {
    }
}
