package pl.locationbasedgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static pl.locationbasedgame.PreferencesHelper.deleteStoredUser;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_start_new_game)
    void onNewGameButton() {
        startActivity(new Intent(MainMenuActivity.this, LobbyActivity.class));
    }

    @OnClick(R.id.rl_settings)
    void onSettingsButton() {
        startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
    }

    @OnClick(R.id.rl_logout)
    void onLogoutButton() {
        Intent backToLoginIntent = new Intent(MainMenuActivity.this, StartActivity.class);
        deleteStoredUser(getApplicationContext());
        finishAffinity();
        startActivity(backToLoginIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
