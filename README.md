# RxGMS for Android
Library that wraps Google Play services (GMS) APIs using RxJava and takes care of connecting and disconnecting the GoogleApiClient for you.
Inspired and extended from [ReactiveLocation](https://github.com/mcharmas/Android-ReactiveLocation).

## Current version - 0.1.0
This version has been developed against Google Play Services 10.0.1 and RxJava 2.0.4. It may work on earlier versions of GMS but any methods deprecated as of 10.0.1 are not included. It will not work with RxJava 1.x.y.
The status of implemented Google Play services APIs is:

| API                                      | Status | Gradle depencency                                     |
|------------------------------------------|--------|-------------------------------------------------------|
| Google+                                  | ✘      | com.google.android.gms:play-services-plus:10.0.1      |
| Google Account Login                     | ✔      | com.google.android.gms:play-services-auth:10.0.1      |
| Google Actions, Base Client Library      | ✔      | com.google.android.gms:play-services-base:10.0.1      |
| Google Address API                       | ✘      | com.google.android.gms:play-services-identity:10.0.1  |
| Firebase App Indexing                    | ✘      | com.google.firebase:firebase-appindexing:10.0.1       |
| Google Analytics                         | ✘      | com.google.android.gms:play-services-analytics:10.0.1 |
| Google Awareness                         | ✘      | com.google.android.gms:play-services-awareness:10.0.1 |
| Google Cast                              | ✔      | com.google.android.gms:play-services-cast:10.0.1      |
| Google Cloud Messaging                   | ✘      | com.google.android.gms:play-services-gcm:10.0.1       |
| Google Drive                             | ✔      | com.google.android.gms:play-services-drive:10.0.1     |
| Google Fit                               | ✘      | com.google.android.gms:play-services-fitness:10.0.1   |
| Google Location and Activity Recognition | ✔      | com.google.android.gms:play-services-location:10.0.1  |
| Google Maps                              | ✘      | com.google.android.gms:play-services-maps:10.0.1      |
| Google Mobile Ads                        | ✘      | com.google.android.gms:play-services-ads:10.0.1       |
| Google Places                            | ✔      | com.google.android.gms:play-services-places:10.0.1    |
| Mobile Vision                            | ✘      | com.google.android.gms:play-services-vision:10.0.1    |
| Google Nearby                            | ✔      | com.google.android.gms:play-services-nearby:10.0.1    |
| Google Panorama Viewer                   | ✘      | com.google.android.gms:play-services-panorama:10.0.1  |
| Google Play Game services                | ✔      | com.google.android.gms:play-services-games:10.0.1     |
| SafetyNet                                | ✘      | com.google.android.gms:play-services-safetynet:10.0.1 |
| Android Pay                              | ✔      | com.google.android.gms:play-services-wallet:10.0.1    |
| Android Wear                             | ✔      | com.google.android.gms:play-services-wearable:10.0.1  |

## Structure
The packages and classes follow a similar structure to the original GMS packages. The API's name is the package and the particular interface in that API is wrapped in a class with the same name prefixed with `Rx`

For example, to use the `LocationServices.FusedLocationApi`, instantiate the class `xyz.fcampbell.rxgms.location.RxFusedLocationApi`. After that, the methods have the same names but return RxJava objects.

All methods return `Observable<R>` except for those that normally return `void` (or `Unit` in Kotlin), which return `Completable`.

## Lifecycle
The connection and disconnection is automatically handled by this library and the subscription/disposal behaviours of RxJava. For methods that return an Observable of a single item (not a stream), it is not necessary to dispose explicitly, since RxJava handles it for you. For streaming data, such as location updates, it is necessary to dispose.

When you wish to terminate a connection (in `onStop()` for example), call `<the api>.disconnect()`

## Installation
Add this to `build.gradle`: `compile 'xyz.fcampbell.rxgms:rxgms:0.1.0'`

This library declares all GMS Gradle dependencies as `provided`, meaning that they are will not be automatically downloaded. This is to avoid pulling in libraries that you don't use and adding methods to your APK. You must therefore specify the GMS libraries that you wish to use. For example, to use location services, add the following line to your `build.gradle`: `compile 'com.google.android.gms:play-services-location:10.0.1'`

Chances are if you get a `NoClassDefFoundError`, it means you're trying to use a GMS API that you haven't added to your `build.gradle` file.

## API Keys
Some GMS APIs require your app to have an API key. If you're already using GMS the old-fashioned way in your app, chances are you've alreay got one. If not, see [https://support.google.com/googleapi/answer/6158862](https://support.google.com/googleapi/answer/6158862)

## API examples
This library is written in Kotlin but works with Java as well, so I've provided both kinds of examples.

### Location

#### Getting last known location

```
val fusedLocationApi = RxFusedLocationApi(context)

fusedLocationApi.getLastLocation()
    .subscribe { location ->
        ...
    }

override fun onStop() {
    fusedLocationApi.disconnect()
}
```

```
RxFusedLocationApi fusedLocationApi = RxFusedLocationApi(context)

fusedLocationApi.getLastLocation()
    .subscribe(new Consumer<Location>() {
        @Override
        public void accept(Location location) {
            ...
        }
    });

@Override
public void onStop() {
    fusedLocationApi.disconnect()
}
```

### Subscribing for location updates
```
val fusedLocationApi = RxFusedLocationApi(context)
val request = LocationRequest.create() //standard GMS LocationRequest
                              .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                              .setNumUpdates(5)
                              .setInterval(100);

val disposable = fusedLocationApi.requestLocationUpdates(request)
    .filter(...)    // you can filter location updates
    .map(...)       // you can map location to something different
    .flatMap(...)   // or even flat map
    ...             // and do everything else that is provided by RxJava
    .subscribe { location ->
        ...
    }

override fun onStop() {
    disposable.dispose()
    fusedLocationApi.disconnect()
}
```

```
LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                  .setNumUpdates(5)
                                  .setInterval(100);

Disposable disposable = new RxFusedLocationApi(context)
    .requestLocationUpdates(request)
    .filter(...)    // you can filter location updates
    .map(...)       // you can map location to something different
    .flatMap(...)   // or even flat map
    ...             // and do everything else that is provided by RxJava
    .subscribe(new Consumer<Location>() {
        @Override
        public void accept(Location location) {
            ...
        }
    });


@Override
public void onStop() {
    disposable.dispose()
    fusedLocationApi.disconnect()
}
```

## Getting the `GoogleApiClient` manually
If you want access to the `GoogleApiClient`, use `Rx<something>` `<api>.getApiClient()` in Java or `<api>.apiClient` in Kotlin to get an `Observable<GoogleApiClient>` that will call `onNext(GoogleApiClient)` once the client is connected to the API that the `Rx<something>` class represents. For example, if you call `fusedLocationApi.getApiClient()`, you'll get a `GoogleApiClient` that is connected to `LocationServices.API` only. You can then use `map`, `flatMap` or other RxJava operators yourself. You should not call `googleApiClient.disconnect()`. Instead, use `<api>.disconnect()`.

## In case of APIs not covered
If a particular GMS API that you use is not yet coverd by this library, you can extend `RxGmsApi` to create your own wrapper. There are some utility methods in `RxGmsApi` class that make it easy to manually wrap a GMS method that is not covered by this library yet. If you come across such a situation, please submit an issue or a pull request so that particular method can be added.

## Sample
Sample usage is available in *sample* directory.

Places API requires API Key. Before running samples you need to create project on API console
and obtain API Key using this [guide](https://developers.google.com/places/android/signup).
Obtained key should be exported as gradle property named: ```REACTIVE_LOCATION_GMS_API_KEY``` for
example in ```~/.gradle/gradle.properties```.


##References
Derived from [ReactiveLocation](https://github.com/mcharmas/Android-ReactiveLocation)

##License
```
Copyright (C) 2017 Francois Campbell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
