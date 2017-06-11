package pl.locationbasedgame;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static pl.locationbasedgame.LobbyActivity.CHASER;
import static pl.locationbasedgame.LobbyActivity.ESCAPER;

public class GameActivity extends Activity {

    static final String TAG = "GAME";
    private static CommunicationService service;
    private boolean isCommunicatorBound;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            GameActivity.service = binder.getService();
            isCommunicatorBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isCommunicatorBound = false;
            Log.i(TAG, "Service connection lost");
        }
    };

    public CommunicationService getService() {
        return service;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        Intent bindIntent = new Intent(this, CommunicationService.class);
        bindService(bindIntent, serviceConnection, Context.BIND_IMPORTANT);

        int team = getIntent().getIntExtra("TEAM", 2);

        if (team == CHASER || team == ESCAPER) {
            loadFragment(team);
        } else {
            Toast.makeText(this, "OOPS!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "You should have never seen that!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCommunicatorBound) {
            unbindService(serviceConnection);
        }
    }

    private void loadFragment(int team) {
        getFragmentManager().beginTransaction()
                .add(R.id.fl_game_module_container, team == CHASER
                        ? new GameChaserFragment()
                        : new GameEscaperFragment())
                .commit();
    }
}
