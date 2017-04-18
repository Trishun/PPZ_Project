package pl.locationbasedgame;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Patryk Ligenza on 14-Apr-17.
 */

class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    ViewPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
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
        if (position == 0) return context.getString(R.string.logon);
        else if (position == 1) return context.getString(R.string.registration);
        return super.getPageTitle(position);
    }
}
