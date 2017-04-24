package pl.locationbasedgame;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Patryk Ligenza on 14-Apr-17.
 */

class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    ViewPagerAdapter(FragmentManager fragmentManager, Context context, LoginFragment loginFragment,
                     RegisterFragment registerFragment) {
        super(fragmentManager);
        this.context = context;
        this.loginFragment = loginFragment;
        this.registerFragment = registerFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return loginFragment;
        else if (position == 1) return registerFragment;
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
