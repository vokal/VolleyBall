package com.vokal.volley;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import java.util.*;

import dagger.*;

import com.vokal.volley.mock.MockHttpStack;

import static dagger.Provides.Type.SET;

@Module(
    library=true
)
public class VolleyBall {

    public static final String MOCK = "Mock";

    public Context mContext;
    public RequestQueue mVolley;
    public String mCurrentKey;
    public Map<String, String> mServers = new HashMap<String, String>();
    public MockHttpStack mMock;

    public VolleyBall(Context aContext) {
        mContext = aContext.getApplicationContext();
        mVolley = Volley.newRequestQueue(mContext);
    }

    public void addServer(String aKey, String aBase) {
        mServers.put(aKey, aBase);
        if (mCurrentKey == null) {
            mCurrentKey = aKey;
        }
    }

    public void addMock(int aRoutesXml) {
        mServers.put(MOCK, "http://mock/");
        mMock = new MockHttpStack(mContext, aRoutesXml);
    }

    @Provides @BaseUrl String provideBase() {
        return mServers.get(mCurrentKey);
    }

    @Provides RequestQueue providesQueue() {
        return mVolley;
    }

    @Provides ServerChanger providesChanger() {
        return new ServerChanger(this);
    }

    void setServer(String aType) {
        if (aType != null && !aType.equals(mCurrentKey) 
                && mServers.keySet().contains(aType)) {
            mCurrentKey = aType;
            
            if (aType.equals(MOCK)) {
                mVolley = Volley.newRequestQueue(mContext, mMock);
            } else {
                mVolley = Volley.newRequestQueue(mContext);
            }
        }
    }

    Set<String> getServerTypes() {
        return mServers.keySet();
    }

    public static class ServerChanger {
        private VolleyBall mModule;
        public ServerChanger(VolleyBall aManager) {
            mModule = aManager;
        }

        public void changeMocks(int aRes) {
            mModule.addMock(aRes);
        }

        public void changeServer(String aKey) {
            mModule.setServer(aKey);
        }

        public String currentServer() {
            return mModule.mCurrentKey;
        }

        public Set<String> getServerTypes() {
            return mModule.getServerTypes();
        }
    }
}
