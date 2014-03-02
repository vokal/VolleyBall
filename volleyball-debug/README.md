Volley Ball Debug Plugin
========
A plugin for use with [VolleyBall](../README.md) and Bill Best's [Barstool] toolbar

![Imgur](http://i.imgur.com/tkRzl7Ol.png)

Example Usage
----

Create necessary modules for otto bus

You must inject `VolleyBallDebug.class` with an [Otto] bus to use it

~~~~java

@Module(
    complete=false,
    injects={
        MyActivity.class,
        VolleyBallDebug.class
    }
)
public class OttoBusModule {
    @Provides @Singleton public Bus otto() {
        return new Bus();
    }
}
~~~~

Create Object Graph

~~~~java
    VolleyBall network = new VolleyBall(this);
    network.addServer("Production", "http://www.google.com/");
    network.addServer("Staging", "http://staging.google.com/");
    network.addMock(R.xml.routes);

    mObjectGraph = ObjectGraph.create(new OttoBusModule(), new
        VolleyBallPlugin(), network);
~~~~

Listen for change callbacks

~~~~java
// In MyActivity

@Inject Bus mBus;

@Subscribe void serverChange(VolleyBallDebug.Changed aEvent) {
    Log.d("MyActivity", "New Server: " + aEvent.server);
}
~~~~

[Barstool]: http://www.github.com/wmbest2/Barstool 
[Otto]: http://square.github.io/otto/ 
