package com.hadilawar.successbits.FragmentsHub;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.hadilawar.successbits.DBHelper;
import com.hadilawar.successbits.R;

import java.util.List;

/**
 * Created by Dilawar on 6/26/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

   // private Speaker speaker;
    private DBHelper dbHelper;
    private List<QuoteData> list;
    boolean isFav;
   public FragmentAdapter(FragmentManager fragmentManager, List<QuoteData> list, Context context) {
        super(fragmentManager);
        this.list =list;
        dbHelper = new DBHelper(context);
    }
    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(position, list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }
    private class CheckAndLoadFavImage extends AsyncTask<Integer,Void,Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //IF QUOTE FAVED
            if(aBoolean == Boolean.TRUE){ }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            if(dbHelper.quoteExist("hgkjhhj"))
                isFav = true;
            else
                return Boolean.FALSE;

            return true;
        }
    }
}
