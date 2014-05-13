Volley Ball Debug Plugin
========
A plugin for use with [VolleyBall](../README.md) and Bill Best's [Barstool] toolbar

![Imgur](http://i.imgur.com/tkRzl7Ol.png)

*NOTE* package has changed to `com.vokal.volley.debug`

Example Usage
----

Create necessary modules for otto bus

You must inject an [Otto] bus to use this plugin

~~~~java

@Module(
    complete=false,
    injects={
        MyActivity.class
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
    network.forEnv("Production").addServer"http://www.google.com/");
    network.forEnv("Staging").addServer("http://staging.google.com/");
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
