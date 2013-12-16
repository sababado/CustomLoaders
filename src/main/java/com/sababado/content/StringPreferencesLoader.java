package com.sababado.content;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * This loader will monitor shared preferences and call back once data has changed under the specified key.
 * The key is defined in the constructor.
 *
 * @see com.sababado.content.StringPreferencesLoader#StringPreferencesLoader(android.content.Context, String)
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StringPreferencesLoader extends AsyncTaskLoader<String> implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final String mKey;
    private String data = null;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void persist(final SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            new Thread() {
                public void run() {
                    editor.commit();
                }
            }.start();
        }
    }

    /**
     * Create a new StringPreferenceLoader with a key for what to monitor.
     *
     * @param context Current context.
     * @param key     Key in shared preferences to look for. Make sure to use this key when saving into shared preferences.
     */
    public StringPreferencesLoader(final Context context, final String key) {
        super(context);
        mKey = key;
    }

    /**
     * Runs on a worker thread, loading in our data.
     */
    @Override
    public String loadInBackground() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        data = prefs.getString(mKey, null);
        return data;
    }


    /**
     * Starts an asynchronous load of the list data.
     * When the result is ready the callbacks will be called
     * on the UI thread. If a previous load has been completed
     * and is still valid the result may be passed to the
     * callbacks immediately.
     * <p/>
     * Must be called from the UI thread.
     */
    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }

        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(mKey)) {
            onContentChanged();
        }
    }
}