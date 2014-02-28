package com.vokal.volley;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import java.util.*;

import barstool.Barstool;
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

    @Provides(type=SET) Barstool.Plugin providesPlugin() {
        return new VolleyDebugger(this);
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

    private static class VolleyDebugger implements Barstool.Plugin {
        VolleyBall mModule;
        public VolleyDebugger(VolleyBall aManager) {
            mModule = aManager;
        }

        public String getTitle() {
            return "Network Settings";
        }

        public View getView(Context aContext, ViewGroup aParent) {
            LayoutInflater inflater = LayoutInflater.from(aContext);
            View view = inflater.inflate(R.layout.barstool, aParent, false);
            Spinner spinner = (Spinner) view.findViewById(R.id.type);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(aContext, android.R.layout.simple_list_item_1);
            adapter.addAll(mModule.getServerTypes());
            spinner.setAdapter(adapter);
            return view;
        }

        public void bindView(View aView, ObjectGraph aGraph) {
            int count = 0;
            for (String i : mModule.getServerTypes()) {
                if (i.equals(mModule.mCurrentKey)) {
                    ((Spinner) aView).setSelection(count);
                    break;
                }
                count++;
            }

            final Spinner spinner = (Spinner) aView;
            spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, 
                        int pos, long id) {
                        mModule.setServer((String) spinner.getAdapter().getItem(pos));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });
        }
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

        public Set<String> getServerTypes() {
            return mModule.getServerTypes();
        }
    }
}
