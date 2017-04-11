package pl.locationbasedgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

interface AuthenticationResultListener {
    void onAuthenticationSuccess();
    void onAuthenticationFailure();
}

public class LoginActivity extends Activity implements AuthenticationResultListener {

    private String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignListeners();
    }

    private void assignListeners() {
        Button registerButton = (Button) findViewById(R.id.btn_register);
        Button loginButton = (Button) findViewById(R.id.btn_login);

        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "PLAYER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        });

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
        EditText nameEditText = (EditText) findViewById(R.id.et_name);
        EditText passwordEditText = (EditText) findViewById(R.id.et_password);

        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (areSpecified(name, password)) {
            sendLoginRequestToServer(name, password);
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.credentials_not_specified, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, R.string.wrong_credentials, Toast.LENGTH_LONG).show();
        Log.i(TAG, "FAIL");
    }

    private void goToMainMenu() {
        Intent mainMenuIntent = new Intent(LoginActivity.this, MainMenuActivity.class);
        finish();
        startActivity(mainMenuIntent);
    }

}
