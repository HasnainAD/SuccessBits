package com.hadilawar.successbits;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilawar on 6/26/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

   // private Speaker speaker;
    private List<QuoteData> list;
    FragmentAdapter(FragmentManager fragmentManager, List<QuoteData> list) {
        super(fragmentManager);
        this.list =list;
        //this.speaker = speaker;
    }
    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(R.layout.fragment_main, list.get(position));

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
