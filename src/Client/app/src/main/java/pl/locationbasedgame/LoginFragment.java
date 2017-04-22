package pl.locationbasedgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment implements AuthenticationResultListener {

    private String TAG = "LOGIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        assignListeners();
    }


    private void assignListeners() {
        Button loginButton = (Button) getView().findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * Send credentials to server if they are specified by user.
     */
    private void login() {
        EditText nameEditText = (EditText) getView().findViewById(R.id.et_name);
        EditText passwordEditText = (EditText) getView().findViewById(R.id.et_password);

        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (areSpecified(name, password)) {
            StartActivity.getService().sendLoginRequestToServer(name, password, this);
        }
        else {
            Toast.makeText(getContext(), R.string.credentials_not_specified, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if two passed strings are not empty.
     */
    private boolean areSpecified(String name, String password) {
        return !name.isEmpty() && !password.isEmpty();
    }

    @Override
    public void onAuthenticationSuccess(String name, String password) {
        Log.i(TAG, "OK");
        storeUser(name, password);
        goToMainMenu();
    }


    @Override
    public void onAuthenticationFailure() {
        Toast.makeText(getContext(), R.string.wrong_credentials, Toast.LENGTH_LONG).show();
        Log.i(TAG, "FAIL");
    }

    private void goToMainMenu() {
        Intent mainMenuIntent = new Intent(getActivity(), MainMenuActivity.class);
        startActivity(mainMenuIntent);
    }

    // Preferences involving methods

    /**
     * Tries to log with previously logged player's credentials.
     *
     * @return true if automatic login request was dispatched to perform, false otherwise
     */
    boolean autoLogin(Context context) {
        // TODO: 21-Apr-17 Consider safer way of storing passwords locally
        Log.i(TAG, "AUTO LOGIN");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = preferences.getString("name", "");
        String password = preferences.getString("password", "");
        if (!name.equals("") && !password.equals("")) {
            StartActivity.getService().sendLoginRequestToServer(name, password, this);
            return true;
        }
        return false;
    }

    private void storeUser(String name, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()
                .getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.apply();
    }
}
