package pl.locationbasedgame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.Locale;
import java.util.concurrent.Callable;

import static butterknife.ButterKnife.findById;
import static pl.locationbasedgame.PreferencesHelper.getStringFromPrefs;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "START";
    private static CommunicationService service;
    private boolean isCommunicatorBound;
    private LoginFragment loginFragment = new LoginFragment();
    private RegisterFragment registerFragment = new RegisterFragment();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            StartActivity.service = binder.getService();

            tryToInitializeSocket();

            isCommunicatorBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isCommunicatorBound = false;
            Log.i(TAG, getResources().getString(R.string.service_connection_lost));
        }
    };

    private void tryToInitializeSocket() {
        Single socketInitializationTask = Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                service.initializeConnection();
                return true;
            }
        });

        socketInitializationTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Boolean b) {
                        Log.i(TAG, getResources().getString(R.string.service_connected));
                        performAutoLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, getResources().getString(R.string.error));
                        e.printStackTrace();
                    }
                });
    }

    private void performAutoLogin() {
        Single<Boolean> autoLoginTask = Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String name = getStringFromPrefs(StartActivity.this, "name");
                String password = getStringFromPrefs(StartActivity.this, "password");
                String locale = Locale.getDefault().toString();

                if (name.equals("") && password.equals("")) {
                    return false;
                } else {
                    AccountResponse result = StartActivity.getService().sendLoginRequestToServer(name, password, locale);
                    return result.isSuccess();
                }
            }
        });

        autoLoginTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(Boolean success) {
                        if (!success) {
                            // Show login page if auto login failed
                            findViewById(R.id.ll_start_screen).setVisibility(View.VISIBLE);
                        } else {
                            loginFragment.goToMainMenu();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, getResources().getString(R.string.error));
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent bindIntent = new Intent(this, CommunicationService.class);
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupTabNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCommunicatorBound) {
            service.sendSimpleMessage("endcon");
            service.closeConnection();
            unbindService(serviceConnection);
        }
    }

    static CommunicationService getService() {
        return service;
    }

    private void setupTabNavigation() {
        ViewPager viewPager = findById(this, R.id.vp_start_screen_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext(),
                loginFragment, registerFragment));

        TabLayout tabLayout = findById(this, R.id.tl_start_screen_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
