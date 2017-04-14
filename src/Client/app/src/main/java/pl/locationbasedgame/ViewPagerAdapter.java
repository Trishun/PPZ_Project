package pl.locationbasedgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Patryk Ligenza on 14-Apr-17.
 */

class ViewPagerAdapter extends FragmentPagerAdapter {

    ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new LoginFragment();
        else if (position == 1) return new RegisterFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Logowanie";
        else if (position == 1) return "Rejestracja";
        return super.getPageTitle(position);
    }
}
