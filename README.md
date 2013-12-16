CustomLoaders
=============

#Usage: `StringPreferencesLoader`
_Based on [Commonsware's `SharedPreferenceLoader`]_

There are two implementations, one for native API Level 11+ development (in the base `com.sababado.content` package) and one for use with the Android Support package (in the `com.sababado.support.v4.content` package).

* Your activity should implement the LoaderManager.LoaderCallbacks<String> interface.
* In your onCreateLoader() method, return an instance of StringPreferencesLoader, which has a two-parameter constructor taking a context and a key in `SharedPreferences` to monitor.
* When data changes in default shared preferences for the key provided in the constructor it will be returned in `onLoadFinished()`.

In addition, there is a static persist() method that takes a `SharedPreferences.Editor` object and arranges to save those edits on a background thread, regardless of Android API level.