package com.vokal.volley;

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.util.*;

import dagger.*;
import barstool.Barstool;

import javax.inject.Inject;

import com.squareup.otto.Bus;

import com.vokal.volley.VolleyBall.EnvSwitcher;

public class VolleyBallDebug implements Barstool.Plugin {

    @Inject Bus mOtto;
    @Inject EnvSwitcher mChanger;

    public String getTitle() {
        return "Network Settings";
    }

    public View getView(Context aContext, ViewGroup aParent) {
        LayoutInflater inflater = LayoutInflater.from(aContext);
        View view = inflater.inflate(R.layout.barstool, aParent, false);

        final Spinner spinner = (Spinner) view.findViewById(R.id.type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(aContext, android.R.layout.simple_list_item_1);
        adapter.addAll(mChanger.getEnvTypes());
        spinner.setAdapter(adapter);

        int count = 0;
        for (String i : mChanger.getEnvTypes()) {
            if (i.equals(mChanger.currentEnv())) {
                spinner.setSelection(count);
                break;
            }
            count++;
        }

        spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
                    String type = (String) spinner.getAdapter().getItem(pos);
                    if (!type.equals(mChanger.currentEnv())) {
                        mChanger.changeEnv(type);
                        mOtto.post(new Changed(type));
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

        return view;
    }

    public static class Changed {
        public String server;
        public Changed(String aType) {
            server = aType;
        }
    }
}

