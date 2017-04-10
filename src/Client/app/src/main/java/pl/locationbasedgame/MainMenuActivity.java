package pl.locationbasedgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout gameButton = (RelativeLayout) findViewById(R.id.rl_start_new_game);
        RelativeLayout settingsButton = (RelativeLayout) findViewById(R.id.rl_settings);
        RelativeLayout logoutButton = (RelativeLayout) findViewById(R.id.rl_logout);

        gameButton.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
            }
        });

        settingsButton.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });

        logoutButton.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
                finishAffinity();
                startActivity(backToLoginIntent);
            }
        });

    }
}