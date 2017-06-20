package pl.locationbasedgame;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.*;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

import static butterknife.ButterKnife.bind;
import static pl.locationbasedgame.LobbyActivity.CHASER;
import static pl.locationbasedgame.LobbyActivity.ESCAPER;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GAME";
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private Fragment fragment;

    private static CommunicationService service;
    private boolean isCommunicatorBound;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            GameActivity.service = binder.getService();
            isCommunicatorBound = true;
            Log.i(TAG, getResources().getString(R.string.service_connected));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isCommunicatorBound = false;
            Log.i(TAG, getResources().getString(R.string.service_connection_lost));
        }
    };

    CommunicationService getService() {
        return service;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bindIntent = new Intent(this, CommunicationService.class);
        bindService(bindIntent, serviceConnection, BIND_IMPORTANT);
        bind(this);

        setPermissions();
        messageListener();

        setContentView(R.layout.activity_game);

        int team = getIntent().getIntExtra("TEAM", -1);

        if (team == CHASER || team == ESCAPER) {
            loadFragment(team);
        } else {
            Toast.makeText(this, getResources().getString(R.string.oops), Toast.LENGTH_SHORT).show();
            Log.i(TAG, getResources().getString(R.string.unexpected_error));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCommunicatorBound) {
            unbindService(serviceConnection);
        }
    }

    @TargetApi(23)
    private void setPermissions() {
        if (Build.VERSION.SDK_INT >= 23)
        requestPermissions(INITIAL_PERMS, 1337);
    }

    private void messageListener() {
        final Single<String> message = Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return service.getServerMessage();
            }
        });

        message.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.i(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.has("coords")) {
                                ((GameChaserFragment) fragment).setDestination(jsonObject.getDouble("locx"), jsonObject.getDouble("locy"));
                            } else if (jsonObject.has("desc")) {
                                String desc = jsonObject.getString("desc");
                                showMessage(desc);
                            } else if (jsonObject.has("task")) {
                                verifyAnswer(jsonObject);
                            } else if (jsonObject.has("gend")) {
                                loadFragment(2);
                            } else if (jsonObject.has("result")) {
                                ((FinalFragment) fragment).showResult(jsonObject.getString("result"));
                            }
                        } catch (JSONException e) {
                            Log.i(TAG, String.valueOf(e));
                        }
                        messageListener();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, String.valueOf(e));
                        messageListener();
                    }
                });
    }

    private void loadFragment(int team) {
        getFragmentManager().beginTransaction()
                .add(R.id.fl_game_module_container, instantiateFragment(team))
                .commit();
    }

    private Fragment instantiateFragment(int team) {
        switch (team) {
            case 0:
                fragment = new GameEscaperFragment();
                break;
            case 1:
                fragment = new GameChaserFragment();
                break;
            case 2:
                fragment = new FinalFragment();
            default:
                break;
        }
        return fragment;
    }

    private void showMessage(String text) {
        final Context context = getApplicationContext();
        final EditText input = new EditText(GameActivity.this);
        final int duration = Toast.LENGTH_SHORT;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_map)
                .setTitle(getText(R.string.new_task))
                .setMessage(text)
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.sendComplexMessage("csans", "ans", input.getText().toString());
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.sent), duration);
                        toast.show();
                    }

                })
                .setNegativeButton("Dismiss", null)
                .show();
    }

    private void showAnswer(final int taskNum, String description, String answer) {
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_map)
                .setTitle(getText(R.string.new_task))
                .setMessage(description + '\n' + answer)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        constructVerifyAnswer(taskNum, true);
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.sent), duration);
                        toast.show();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        constructVerifyAnswer(taskNum, false);
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.sent), duration);
                        toast.show();
                    }

                })
                .show();
    }

    private void constructVerifyAnswer(int tasknum, boolean var) {
        try {
            JSONObject object = new JSONObject();
            object.put("header", "cvans");
            object.put("cnum", tasknum);
            object.put("var", var);
            service.sendComplexMessage(object);
        } catch (JSONException e) {
            Log.i(TAG, "Something went wrong");
        }
    }

    private void verifyAnswer(JSONObject jsonObject) {
        try {
            final int taskNum = jsonObject.getInt("task");
            final String description = jsonObject.getString("desc");
            final String answer = jsonObject.getString("ans");

            final Snackbar mySnackbar = Snackbar.make(((GameEscaperFragment) fragment).getRootView(), "Got new task!", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("Show", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAnswer(taskNum, description, answer);
                }
            });
            mySnackbar.show();
        } catch (Exception e) {
            Log.i(TAG, String.valueOf(e));
        }

    }

}
