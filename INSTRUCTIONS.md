#Developer Instructions
With these instructions, we hope you can quickly become accustomed to using aContextLib. There is a sample application in the module `sample` module of this project. Use this as further examples on how to use the different components.

##Linking with your application
To use this library, you can either include the sourcecode in your application, or alternatively, include this library through the use of Gradle dependencies:
```gradle
dependencies {
     compile 'uk.ac.mdx.cs.ie:acontextlib:0.9.8'
}
```

Or Maven:
```maven
   <dependency>
     <groupId>uk.ac.mdx.cs.ie</groupId>
     <artifactId>acontextlib</artifactId>
     <version>0.9.8</version>
     <type>pom</type>
   </dependency>
```

##Usage in your application
In this library there are two dominant types of components:
 * ContextObserver - A component designed to collect the raw data from a particular sensor of data source
 * ContextReceiver - A callback designed for being sent this raw data back from the ContextObserver

 ### ContextReceivers
 ContextReceivers are designed to get raw data send to them in a unified callback for the application. There is two specific variants:
 * `IContextReceiver` - an Interface designed for applications that are using the observers directly in their applications. This interface functions as a unified callback for the data collection.
 * `ContextReceiver` - an Abstract class designed for applications using the [POSEIDON-Context](https://github.com/deankramer/POSEIDON-Context) Context reasoner. Using this abstract class you can extend it to your own new observers before importing into the context reasoner itself.

 #### Context Receiver Usage
 As stated above, depending on your use case, you will either want to extend the `ContextReceiver` class for use in the [POSEIDON-Context](http://github.com/deankramer/POSEIDON-Context) Context-Awareness Reasoner, or implement the `IContextReceiver` interface for using just the observers in your applications.

 To assist you in know what observer the data has originated from, each Observer has a static final String named with the prefix `RECEIVER_`. This will contain the name which is sent to the context receiver.


 ```java
IContextReceiver myContextReceiver = new IContextReceiver() {
            @Override
            public void newContextValue(String name, long value) {
                //Your code for getting long values
                if (name.equals(LightObserver.RECEIVER_LIGHT)) {
                    //Do something
                }
            }

            @Override
            public void newContextValue(String name, double value) {
                //Your code for getting double values
            }
            @Override
            public void newContextValue(String name, boolean value) {
                //Your code for getting boolean values
            }

            @Override
            public void newContextValue(String name, String value) {
                //Your code for getting String values
            }

            @Override
            public void newContextValue(String name, Object value) {
                //Your code for getting Object values
            }

            @Override
            public void newContextValues(Map<String, String> values) {
                //Your code for getting Map values
            }
        });

 ```
 ### ContextObservers
 ContextObservers are designed as the primary components for the collection of raw data from Android sensors and datasources. As such, each specific ContextObserver will extend one of the following abstract classes:
 * `ContextObserver` - The core abstract class for all observers.
 * `PullObserver` - An observer designed for timer based data retrieval.
 * `PushObserver` - A small abstract classed used for all non pull based observers
 * `SensorObserver` - Used for getting data from Android device sensors directly
 * `BroadcastObserver` - Used for getting data from Android device intent broadcasts
 * `BluetoothLEObserver` - Used for getting data from Bluetooth Low Energy devices
 * `LocationObserver` - Used for getting data from Location services

 #### Context Observer Lifecycle
 All context observers contain the following lifecycle methods:
 * `start()` - The method called when you wish to start collecting particular data from that observer
 * `pause()` - The method called for temporary halting of data collection
 * `resume()` - The method called to resume data collection
 * `stop()` - The method called to cease data collection.

 #### Context Observer Usage
 All context observers require an Android [`Context`](https://developer.android.com/reference/android/content/Context.html) instance in all constructors.

```java
//Construct new Observer instance
Context context = getApplicationContext();
LightObserver lightlevel = new LightObserver(context);

//Bind a ContextReceiver to the observer
lightlevel.addContextReceiver(myContextReceiver);

//Start the Observer
lightlevel.start();

//Stop the Observer
lightlevel.stop();
```
