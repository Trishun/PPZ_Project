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

public class StartActivity extends AppCompatActivity {

    private static CommunicationService service;
    private static final String TAG = "START";
    private boolean isCommunicatorBound;
    private LoginFragment loginFragment = new LoginFragment();
    private RegisterFragment registerFragment = new RegisterFragment();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            StartActivity.service = binder.getService();
            isCommunicatorBound = true;
            if (!loginFragment.autoLogin(getApplicationContext())) {
                findViewById(R.id.ll_start_screen).setVisibility(View.VISIBLE);
            }
            Log.i(TAG, "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isCommunicatorBound = false;
            Log.i(TAG, "Service connection lost");
        }
    };

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
            service.closeConnection();
            unbindService(serviceConnection);
        }
    }

    static CommunicationService getService() {
        return service;
    }

    private void setupTabNavigation() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_start_screen_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext(),
                loginFragment, registerFragment));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_start_screen_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
