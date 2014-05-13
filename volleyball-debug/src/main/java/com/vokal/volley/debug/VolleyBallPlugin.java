package com.vokal.volley.debug;

import dagger.*;
import barstool.*;

@Module(
    complete=false,
    includes=BarstoolModule.class
)
public class VolleyBallPlugin {
    @Provides(type=Provides.Type.SET) Barstool.Plugin debugger(VolleyBallDebug debug) {
        return debug;
    }
}
