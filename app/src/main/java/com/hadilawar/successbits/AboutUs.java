package com.hadilawar.successbits;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {


    private TextView aboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutUsTool);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        aboutTextView= (TextView) findViewById(R.id.aboutUsTextView);

        Typeface typo = Typeface.createFromAsset(getAssets(), "fonts/lemonada-regular.ttf");
        aboutTextView.setTypeface(typo);


    }
}
