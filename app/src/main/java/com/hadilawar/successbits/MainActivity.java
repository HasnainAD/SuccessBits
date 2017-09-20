package com.hadilawar.successbits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hadilawar.successbits.FavoritesHub.FavoriteActivity;
import com.hadilawar.successbits.FragmentsHub.FragmentAdapter;
import com.hadilawar.successbits.FragmentsHub.QuoteData;
import com.hadilawar.successbits.Firebases.FBUtils;
import com.hadilawar.successbits.Others.Speaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private QuoteData quoteData;
    private final int CHECK_CODE = 0x1;
    private List<QuoteData> list = new ArrayList<>();
    private SharedPreferences sharedPref;// = getActivity().getPreferences(Context.MODE_PRIVATE);
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;//
    private Speaker speaker;
    private TextView titleText;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean activityAvailable;

    private RelativeLayout alternateView;
    private  ProgressBar progressBar;
    private View rootView;
    private View connectionText;
    private View connectionImage;
    private View refreshText;
    private ProgressBar probar;
    Snackbar mySnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //IF NETWORK IS CONNECTED
        if(isNetworkConnected()){
            Toast.makeText(this,"Internet working", Toast.LENGTH_SHORT);
                setUpActivity();
        }
        //IF INTERNET IS NOT CONNECTED
        else{
            prefs = getSharedPreferences("com.hadilawar.successbits", MODE_PRIVATE);
            //FIRST RUN OF APP
            if (prefs.getBoolean("firstrun", true)){
                // final Handler myHandler = new Handler();
                setContentView(R.layout.activity_main_alternate);
                View view = findViewById(R.id.alternate);
                connectionImage = findViewById(R.id.wifi);
                connectionText = findViewById(R.id.connectionText);
                refreshText = findViewById(R.id.refreshText);


                        view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("INSIDE CLICK","ITS REALLY WORKING");
                        Toast.makeText(MainActivity.this,"First run, click, Internet working", Toast.LENGTH_SHORT);
                        progressBar = (ProgressBar) (View)findViewById(R.id.progressBar);
                        if(isNetworkConnected()){
                            Toast.makeText(MainActivity.this,"First run, click, Internet working", Toast.LENGTH_SHORT);
                            new updateTask2().execute();
                            prefs.edit().putBoolean("firstrun", false).commit();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "First run, click, Internet not working", Toast.LENGTH_SHORT).show();
                            new updateTask().execute();
                            prefs.edit().putBoolean("firstrun", true).commit();
                        }
                    }
                });
            }
            //NOT FIRST RUN OF APP
            else{
            setUpActivity();
            }
        }

    }



 //SETTING MAIN ACIVITY CONTENT
    private  void setUpActivity(){

        setContentView(R.layout.activity_main);
        Log.e("IN Fucntion", "MAIN");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//      titleText =(TextView) findViewById(R.id.appnametext);

        probar = (ProgressBar) findViewById(R.id.mprobar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        navigationView.setNavigationItemSelectedListener(new NavigationItemListener());
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
//        titleText.setTypeface(typeface);
        mFirebaseDatabase = FBUtils.getDatabase();
        mDatabaseReference = mFirebaseDatabase.getReference().child("quotes");

        //CHECK AND INSTALL TTS
        boolean ttsInstalled = sharedPref.getBoolean("TTS", false);
        if (!ttsInstalled)
            checkTTS();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_favorite: {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    case R.id.nav_rateus: {
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(rateIntent);
                        return true;
                    }
                    case R.id.nav_aboutus: {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, AboutUs.class));
                        return true;
                    }
                    case R.id.nav_share: {


                    }
                    case R.id.nav_home: {


                    }
                    default:
                    {}



                }

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
               // Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

       // navigationView.setNavigationItemSelectedListener(new NavigationItemListener());

        //GET AND SET VIEWPAGER
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new FragmentAdapter(getFragmentManager(), list, this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(7);
        mDatabaseReference.orderByValue().limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                probar.setVisibility(View.INVISIBLE);
                quoteData = dataSnapshot.getValue(QuoteData.class);
                list.add(quoteData);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(list.size() - 1, true);
                  Log.e("On Child ADDED","On Child ADDED");
                // TODO: 8/20/2017  fetch

                if (list.size() >= 2)
                    Toast.makeText(MainActivity.this, quoteData.getQuote(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        new DelayPop().execute();

    }

 //ONCLICK OF SHOW NEXT OR SHOW CURRENT ACTIVITY
//    @Override
//    public void onClick(View v) {
//        if(v.getId() == R.id.prevImage)
//            viewPager.setCurrentItem(getItem(-1), true);
//        else if(v.getId() == R.id.nextImage){}
//            viewPager.setCurrentItem(getItem(1), true);
//    }



    ////SHOWS SNACKBAR IF NOT CONNECTED TO INTERNET
   private class DelayPop extends  AsyncTask<Void,Void,Void>
   {
       @Override
       protected Void doInBackground(Void... params) {
           try {
               Log.e("BACKGROUND"," thread");
               Thread.sleep(5000);
           } catch(InterruptedException ex) {
               Thread.currentThread().interrupt();
           }
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           Log.e("POST EXECUTE"," thread");

           Log.e("COUNTER", Integer.toString(adapter.getCount()));
           if( adapter.getCount() <= 0) {
               mySnackbar = Snackbar.make(findViewById(R.id.mCordinate),
                       "Looks Like There is Connection Issue, Check connection and try again", 4000);
               mySnackbar.show();
           }
           super.onPostExecute(aVoid);
       }
   }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void checkTTS(){
        if(activityAvailable){
            Intent check = new Intent();
            check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(check, CHECK_CODE);
          }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(activityAvailable){
            if(requestCode == CHECK_CODE){
                if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                    editor = sharedPref.edit();
                    editor.putBoolean("TTS", true);
                    editor.commit();
                }else {
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(activityAvailable)
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class NavigationItemListener implements NavigationView.OnNavigationItemSelectedListener{


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if(menuItem.getItemId() == R.id.nav_favorite) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
            else if(menuItem.getItemId() == R.id.nav_rateus){
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(rateIntent);
                return true;
            }
            else if(menuItem.getItemId() == R.id.nav_aboutus){
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                return true;
            }
            else if(menuItem.getItemId() == R.id.nav_share){


            }
            else  if(menuItem.getItemId() == R.id.nav_home){

                if(true){}

            }
            return false;
        }
    }

    // FOR FIRST TIME AND NOT CONNECTED TO INTERNET
    private class updateTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            connectionText.setVisibility(View.INVISIBLE);
            connectionImage.setVisibility(View.INVISIBLE);
            refreshText.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            connectionText.setVisibility(View.VISIBLE);
            connectionImage.setVisibility(View.VISIBLE);
            refreshText.setVisibility(View.VISIBLE);
//            Toast.makeText(MainActivity)
        }

        @Override
        protected Void doInBackground(Void... params) {
            long delayInMillis = 3000;
            Timer timer = new Timer();
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //FOR FIRST TIME AND CONNECTED TO INTERNET,
    //CHANGE THE CONTENT VIEW OF THE ACTIVITY
    //BY CALLING SETUPACTIVITY()

    private class updateTask2 extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            try {
                progressBar.setVisibility(View.VISIBLE);
                connectionText.setVisibility(View.INVISIBLE);
                connectionImage.setVisibility(View.INVISIBLE);
                refreshText.setVisibility(View.INVISIBLE);
            }catch(Exception e){

                Log.e("ERRRROR", e.getMessage());
                Log.e("ERRRROR", e.getMessage());
                Log.e("ERRRROR", e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            try {
                progressBar.setVisibility(View.INVISIBLE);
                connectionText.setVisibility(View.VISIBLE);
                connectionImage.setVisibility(View.VISIBLE);
                refreshText.setVisibility(View.VISIBLE);
            }catch(Exception e){
                Log.e("ERRRROR", e.getMessage());
                Log.e("ERRRROR", e.getMessage());
                Log.e("ERRRROR", e.getMessage());
            }
            setUpActivity();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long delayInMillis = 2000;
            Timer timer = new Timer();
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
