package pl.locationbasedgame;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setupTabNavigation();
    }

    private void setupTabNavigation() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_start_screen_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_start_screen_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
