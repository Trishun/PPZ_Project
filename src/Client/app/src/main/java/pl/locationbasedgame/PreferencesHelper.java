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
     * Tries to log with previously logged player's credentials.
     *
     * @return true if automatic login request was dispatched to perform, false otherwise
     */
    static boolean autoLogin(Context context) {
        // TODO: 21-Apr-17 Consider safer way of storing passwords locally
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = preferences.getString("name", "");
        String password = preferences.getString("password", "");
        String locale = Locale.getDefault().toString();

        return !name.equals("") && !password.equals("") && StartActivity.getService().sendLoginRequestToServer(name, password, locale);
    }
}
