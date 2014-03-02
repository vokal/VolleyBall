Volley Ball
========

A drop-in Volley module for use with Square's [Dagger](http://square.github.io/dagger) 

Features
----

* Easily Switch between multiple server types on the fly
* Add Mocks through use of a custom <routes> xml file
* Simple provides to make fixed object injection easy to replicate in production version
* [Additional plugin](volleyball-debug/README.md) available for [Barstool] if used

Usage
----

Create your module

~~~~java
public class MyApplication {

    public void onCreate() {
        super.onCreate();

        VolleyBall network = new VolleyBall(this);
        network.forEnv("Production").addServer"http://www.google.com/");
        network.forEnv("Staging").addServer("http://staging.google.com/");
        network.addMocks(R.xml.routes);

        mObjectGraph = ObjectGraph.create(new MainModule(), network);
    }
}
~~~~

Setup your mocks

~~~~xml
<routes>
    <!-- Parameterized Asset Files -->
    <route type="get" path="feed/news?p={page}">newsfeed_{page}.xml</route>

    <!-- Static HTTP codes -->
    <route type="post" path="feed/news/create">201</route>

    <!-- Strings -->
    <route type="get" path="profile/name">R.string.test_name</route>
    
    <!-- And Raw Files -->
    <route type="put" path="profile/update">R.raw.user</route>
</routes>
~~~~

Inject your RequestQueue

~~~~java
public class VolleyNewsFeedService implements NewsFeedService {
    @Inject RequestQueue mRequestQueue;
    @Inject @BaseUrl String mBaseUrl;

    public void fetchPage(int aPage) {
        ...
    }
}
~~~~

Multple Servers per Environment

~~~~java
public class MyApplication {

    public void onCreate() {
        super.onCreate();

        VolleyBall network = new VolleyBall(this);
        network.forEnv("Production").addServer("main", "http://www.google.com/");
        network.forEnv("Production").addServer("twitter", "http://www.twitter.com/");
        network.forEnv("Staging").addServer("main", "http://staging.google.com/");
        network.forEnv("Staging").addServer("twitter", "http://www.twitter.com/");
        network.addMocks(R.xml.routes);

        mObjectGraph = ObjectGraph.create(new MainModule(), network);
    }
}
~~~~

Inject your RequestQueue and Environment

~~~~java
public class VolleyNewsFeedService implements NewsFeedService {
    @Inject RequestQueue mRequestQueue;
    @Inject EnvMap mMap;

    public void fetchPage(int aPage) {
        ...
    }
}
~~~~

OR

~~~~java
// In Module
@Provides @Named("main") String provideMain(EnvMap aMap) {
    return aMap.get("main");
}

@Provides @Named("twitter") String providesTwitter(EnvMap aMap) {
    return aMap.get("twitter");
}

// In Service

public class VolleyNewsFeedService implements NewsFeedService {
    @Inject RequestQueue mRequestQueue;
    @Inject @Named("main") String mBaseUrl;

    public void fetchPage(int aPage) {
        ...
    }
}

~~~~

Notes and Gotchas
----

VolleyBall is not a standalone module, it is indended to be used with
another module even if it is empty. Because of this you need mark your module
as incomplete like so.

~~~~java
@Module(
    complete=false,
    injects=VolleyNewsFeedService.class
)
public class MainModule {

}
~~~~

[Barstool]: http://www.github.com/wmbest2/Barstool 
