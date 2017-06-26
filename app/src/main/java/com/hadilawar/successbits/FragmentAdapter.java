package com.hadilawar.successbits;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Dilawar on 6/26/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    FragmentAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(R.layout.fragment_main);

    }

    @Override
    public int getCount() {
        return 7;
    }
}
