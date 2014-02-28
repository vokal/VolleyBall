package com.test;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.TextView;

import javax.inject.Inject;

import barstool.Barstool;

import butterknife.*;

import com.android.volley.RequestQueue;
import com.vokal.volley.BaseUrl;
import com.vokal.volley.VolleyModule.ServerChanger;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.response) TextView mResponse;

    @Inject @BaseUrl String mBase;
    @Inject RequestQueue mVolley;
    @Inject ServerChanger mChanger;

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.main);

        ButterKnife.inject(this);

        ((TestApp) getApplication()).graph().inject(this);
        Barstool.with(((TestApp) getApplication()).graph()).wrap(this);
    }

    @OnClick(R.id.make_request)
    public void request() {
        ((TestApp) getApplication()).graph().inject(this);
        JsonObjectRequest req = new JsonObjectRequest(mBase + "my/request/test?p=1", null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        android.util.Log.d("MainActivity", response.getString("test"));
                        mResponse.setText(String.valueOf(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    android.util.Log.e("MainActivity", String.valueOf(error));
                }
            });
        mVolley.add(req);
    }
}
