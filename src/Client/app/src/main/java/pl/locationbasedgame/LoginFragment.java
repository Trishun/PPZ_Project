package pl.locationbasedgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
            sendLoginRequestToServer(name, password);
        }
        else {
            Toast.makeText(getActivity(), R.string.credentials_not_specified, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if two passed strings are not empty.
     */
    private boolean areSpecified(String name, String password) {
        return !name.isEmpty() && !password.isEmpty();
    }

    private void sendLoginRequestToServer(String name, String password) {
        Authenticator authenticator = new Authenticator();
        authenticator.setCaller(this);
        authenticator.execute(name, password);
    }

    @Override
    public void onAuthenticationSuccess() {
        Log.i(TAG, "OK");
        goToMainMenu();
    }

    @Override
    public void onAuthenticationFailure() {
        Toast.makeText(getActivity(), R.string.wrong_credentials, Toast.LENGTH_LONG).show();
        Log.i(TAG, "FAIL");
    }

    private void goToMainMenu() {
        Intent mainMenuIntent = new Intent(getActivity(), MainMenuActivity.class);
        getActivity().finish();
        startActivity(mainMenuIntent);
    }
}