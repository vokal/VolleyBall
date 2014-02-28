package com.vokal.volley;

import dagger.*;
import barstool.*;

@Module(
    includes=BarstoolModule.class
)
public class VolleyPluginModule {
    @Provides(type=Provides.Type.SET) Barstool.Plugin debugger() {
        return new VolleyPluginModule();
    }
}
