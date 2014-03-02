package com.vokal.volley;

import dagger.*;
import barstool.*;

@Module(
    includes=BarstoolModule.class
)
public class VolleyBallPlugin {
    @Provides(type=Provides.Type.SET) Barstool.Plugin debugger() {
        return new VolleyBallDebug();
    }
}
