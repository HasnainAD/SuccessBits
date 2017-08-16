package com.hadilawar.successbits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.hadilawar.successbits", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {

            if(isNetworkConnected()){
                setContentView(R.layout.activity_main);
                activityAvailable = true;
                prefs.edit().putBoolean("firstrun", false).commit();
            }
            else {
                setContentView(R.layout.activity_main_alternate);
                activityAvailable = false;
                prefs.edit().putBoolean("firstrun", true).commit();
            }

        }
        else{
            setContentView(R.layout.activity_main);
            activityAvailable = true;
        }

        if (activityAvailable){
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
//        titleText =(TextView) findViewById(R.id.appnametext);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            navigationView.setNavigationItemSelectedListener(new NavigationItemListener());
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
//        titleText.setTypeface(typeface);
            mFirebaseDatabase = FBUtils.getDatabase();
            mDatabaseReference = mFirebaseDatabase.getReference().child("quotes");

            //check and Install TTS
            boolean ttsInstalled = sharedPref.getBoolean("TTS", false);
            if (!ttsInstalled)
                checkTTS();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                    Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                    return true;
                }
            });


            viewPager = (ViewPager) findViewById(R.id.pager);
            adapter = new FragmentAdapter(getFragmentManager(), list);
            viewPager.setAdapter(adapter);
            mDatabaseReference.orderByValue().limitToLast(7).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    quoteData = dataSnapshot.getValue(QuoteData.class);
                    list.add(quoteData);
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(list.size() - 1, true);
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
}
