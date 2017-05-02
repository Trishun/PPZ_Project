package pl.locationbasedgame;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LobbyActivity extends Activity implements LobbyTaskCallback {

    private String TAG = "LOBBY";
    private CommunicationService service;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            LobbyActivity.this.service = binder.getService();
            isServiceBound = true;

            Log.i(TAG, "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
            Log.i(TAG, "Service connection lost");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        assignListeners();
        Intent bindIntent = new Intent(this, CommunicationService.class);
        bindService(bindIntent, serviceConnection, BIND_IMPORTANT);
    }

    private void assignListeners() {
        Button newGameButton = (Button) findViewById(R.id.btn_create_lobby);
        Button joinGameButton = (Button) findViewById(R.id.btn_join_lobby);

        newGameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.createNewLobby(new LobbyTask(), LobbyActivity.this);
            }
        });

        joinGameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText codeEditText = (EditText) findViewById(R.id.et_lobby_id);
                String content = codeEditText.getText().toString();
                if (!content.isEmpty()) {
                    int lobbyId = Integer.parseInt(content);
                    service.joinExistingLobby(new LobbyTask(), LobbyActivity.this, lobbyId);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onLobbyCreated(int id) {
        Log.i("CODE", String.valueOf(id));
        //proceed
    }

    @Override
    public void onLobbyJoined() {
        Log.i(TAG, "JOIN");
        //proceed
    }
}
