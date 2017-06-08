package pl.locationbasedgame;

import android.app.Activity;
import android.os.Bundle;

import static pl.locationbasedgame.LobbyActivity.CHASER;

public class GameActivity extends Activity {

    static final String TAG = "GAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        loadLobbyRootFragment();
//        int team = getIntent().getIntExtra("TEAM", 2);
//
//        if (team == CHASER || team == ESCAPER) {
//            loadFragment(team);
//        } else {
//            Toast.makeText(this, "OOPS!", Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "You should have never seen that!");
//        }
    }

    private void loadLobbyRootFragment() {
        getFragmentManager().beginTransaction().add(R.id.fl_game_module_container, new LobbyRootFragment()).commit();
    }

    private void loadFragment(int team) {
        getFragmentManager().beginTransaction()
                .add(R.id.fl_game_module_container, team == CHASER
                        ? new GameChaserFragment()
                        : new GameEscaperFragment())
                .commit();
    }
}
