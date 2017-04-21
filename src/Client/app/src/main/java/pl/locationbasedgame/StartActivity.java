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

public class StartActivity extends AppCompatActivity {

    private static CommunicationService service;
    private static final String TAG = "START";
    private boolean isCommunicatorBound;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.ServerBinder binder = (CommunicationService.ServerBinder) service;
            StartActivity.service = binder.getService();
            isCommunicatorBound = true;
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
        setContentView(R.layout.activity_start);
        setupTabNavigation();
        Intent bindIntent = new Intent(this, CommunicationService.class);
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_start_screen_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
