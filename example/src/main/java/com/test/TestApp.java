package com.test;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.*;
import barstool.*;

import com.vokal.volley.VolleyBall;
import com.vokal.volley.VolleyBallDebug;

public class TestApp extends Application {

    public ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyBall module = new VolleyBall(this);
        module.addServer("Production", "http://www.google.com/");
        module.addServer("Staging", "http://staging.google.com/");
        module.addMock(R.xml.routes);

        mObjectGraph = ObjectGraph.create(new DummyModule(), new VolleyBallPlugin(), module);
        
    }

    public ObjectGraph graph() {
        return mObjectGraph;
    }

    @Module(
        complete=false,
        injects={
            MainActivity.class,
            VolleyBallDebug.class
        }
    )
    public static class DummyModule {
        @Provides @Singleton Bus ottoBus() {
            return new Bus();
        }
    }
}
