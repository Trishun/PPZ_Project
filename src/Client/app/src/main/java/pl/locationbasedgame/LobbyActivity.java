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

import org.json.JSONArray;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LobbyActivity extends Activity {

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
                createLobby();
            }
        });

        joinGameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText codeEditText = (EditText) findViewById(R.id.et_lobby_id);

                String content = codeEditText.getText().toString();

                if (!content.isEmpty()) {
                    final int lobbyId = Integer.parseInt(content);
                    createLobby(lobbyId);
                }
            }
        });
    }

    private void createLobby() {
        Single<Integer> createTask = Single.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return service.createNewLobby(new LobbyManager());
            }
        });

        createTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Integer integer) {
                          Log.i(TAG, "OK: " + integer.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ERROR", e.getMessage());
                    }
                });
    }

    private void createLobby(final int lobbyId) {
        Single<JSONArray> getPlayerList = Single.fromCallable(new Callable<JSONArray>() {
            @Override
            public JSONArray call() throws Exception {
                return service.joinExistingLobby(new LobbyManager(), lobbyId);
            }
        });

        getPlayerList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<JSONArray>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.i(TAG, jsonArray.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "error");
                        e.printStackTrace();
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
}
