package pl.locationbasedgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Patryk Ligenza on 03-May-17.
 */

class PreferencesHelper {

    // Login/Logout methods

    static void deleteStoredUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("name");
        editor.remove("password");
        editor.apply();
    }

    static void storeUser(Context context, String name, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.apply();
    }

    /**
     * Gets value from SharedPreferences associated with key.
     *
     * @return value from SP if present, empty String otherwise.
     */
    static String getStringFromPrefs(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }
}
