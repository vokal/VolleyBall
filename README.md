Volley Ball
========

A drop-in Volley module for use with Square's [Dagger](http://square.github.io/dagger) and Bill Best's [Barstool] toolbar

Features
----

* Easily Switch between multiple server types on the fly
* Add Mocks through use of a custom <routes> xml file
* Simple provides to make fixed object injection easy to replicate in production version
* Automatically adds itself to [Barstool] if used

Usage
----

Create your module

~~~~java
public class MyApplication {

    public void onCreate() {
        super.onCreate();

        VolleyModule network = new VolleyModule(this);
        network.addServer("Local", "http://10.1.10.176/");
        network.addServer("Staging", "http://staging.myapp.com/")
        network.addServer("Production", "https://www.myapp.com/")
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

Notes and Gotchas
----

The VolleyModule is not a standalone module, it is indended to be used with
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
