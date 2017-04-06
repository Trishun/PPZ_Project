package pl.locationbasedgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity {

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
                Intent mainMenuIntent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });
    }
}
