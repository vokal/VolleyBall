package com.test;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.*;
import barstool.*;

import com.vokal.volley.VolleyBall;
import com.vokal.volley.debug.VolleyBallDebug;
import com.vokal.volley.debug.VolleyBallPlugin;

public class TestApp extends Application {

    public ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyBall module = new VolleyBall(this);
        module.forEnv("Production").addServer("http://www.google.com/");
        module.forEnv("Staging").addServer("http://staging.google.com/");
        module.addMock(R.xml.routes);

        mObjectGraph = ObjectGraph.create(new DummyModule(), module, new VolleyBallPlugin());
    }

    public ObjectGraph graph() {
        return mObjectGraph;
    }

    @Module(
        complete=false,
        injects=MainActivity.class
    )
    public static class DummyModule {
        @Provides @Singleton Bus ottoBus() {
            return new Bus();
        }
    }
}
