package com.hadilawar.successbits.FavoritesHub;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hadilawar.successbits.DBHelper;
import com.hadilawar.successbits.R;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {"hasnian", "dilawar","nagari", "bulzne", "dilawar","nagari", "bulzne", "dilawar","nagari", "bulzne"};
    private ArrayList<FavItem> array;
    private FavsRetriever favsRetriever;
    private DBHelper dbHelper;
    private  Typeface typeface;
    private TextView emptyTextView;
    private  ActionBar actionBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_acitvity);
        dbHelper = new DBHelper(this);
        array = dbHelper.getAll();
        typeface = Typeface.createFromAsset(getAssets(), "fonts/lemonada-semibold.ttf");
        emptyTextView.setTypeface(typeface);


        toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        if(array.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
        }
        else{
            emptyTextView.setVisibility(View.GONE);
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setAdapter(new RVAdapter(array, this));
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

        }


    }

    private class FavsRetriever extends AsyncTask<Void, Void ,ArrayList<FavItem>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<FavItem> doInBackground(Void... params) {


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<FavItem> favItems) {

            //super.onPostExecute(favItems);
        }
    }
}
