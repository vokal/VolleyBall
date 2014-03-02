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

    public static final String DEFAULT_URL = "default_url";
    public static final String MOCK_BASE = "http://mock/";
    public static final String MOCK = "Mock";

    public Context mContext;
    public RequestQueue mVolley;
    public String mCurrentKey;
    public Map<String, EnvMap> mEnvironments = new HashMap<String, EnvMap>();
    public MockHttpStack mMock;

    public VolleyBall(Context aContext) {
        mContext = aContext.getApplicationContext();
        mVolley = Volley.newRequestQueue(mContext);
    }

    public EnvBuilder forEnv(String aKey) {
        EnvBuilder builder = new EnvBuilder();

        if (!mEnvironments.containsKey(aKey)) {
            EnvMap map = new EnvMap();
            builder.map = map;
            mEnvironments.put(aKey, map);
        } else {
            builder.map = mEnvironments.get(aKey);
        }

        if (mCurrentKey == null) {
            mCurrentKey = aKey;
        }

        return builder;
    }

    public void addMock(int aRoutesXml) {
        mEnvironments.put(MOCK, new MockMap());
        mMock = new MockHttpStack(mContext, aRoutesXml);
    }

    @Provides @BaseUrl String provideBase(EnvMap aMap) {
        return aMap.get(DEFAULT_URL);
    }

    @Provides EnvMap provideUrls() {
        return mEnvironments.get(mCurrentKey);
    }

    @Provides RequestQueue providesQueue() {
        return mVolley;
    }

    @Provides EnvSwitcher providesChanger() {
        return new EnvSwitcher(this);
    }

    void setEnv(String aType) {
        if (aType != null && !aType.equals(mCurrentKey) 
                && mEnvironments.keySet().contains(aType)) {
            mCurrentKey = aType;
            
            if (aType.equals(MOCK)) {
                mVolley = Volley.newRequestQueue(mContext, mMock);
            } else {
                mVolley = Volley.newRequestQueue(mContext);
            }
        }
    }

    Set<String> getEnvTypes() {
        return mEnvironments.keySet();
    }

    public static class EnvSwitcher {
        private VolleyBall mModule;
        public EnvSwitcher(VolleyBall aManager) {
            mModule = aManager;
        }

        public void changeEnv(String aKey) {
            mModule.setEnv(aKey);
        }

        public String currentEnv() {
            return mModule.mCurrentKey;
        }

        public Set<String> getEnvTypes() {
            return mModule.getEnvTypes();
        }
    }

    public static class EnvMap extends HashMap<String, String> {}

    public static class MockMap extends EnvMap {
        public String get(String aKey) {
            return MOCK_BASE;
        }
    }

    public static class EnvBuilder {
        public EnvMap map;
        public EnvBuilder addServer(String aKey, String aUrl) {
            if (map.size() == 0) {
                map.put(DEFAULT_URL, aUrl);
            }
            map.put(aKey, aUrl);
            return this;
        }

        public EnvBuilder addServer(String aUrl) {
            map.put(DEFAULT_URL, aUrl);
            return this;
        }
    }
}
