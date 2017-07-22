package com.hadilawar.successbits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private  FragmentAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private QuoteData quoteData;
    private final int CHECK_CODE = 0x1;
    private List<QuoteData> list = new ArrayList<>();
    private SharedPreferences sharedPref;// = getActivity().getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor;//
    private Speaker speaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mFirebaseDatabase = Utils.getDatabase();
        mDatabaseReference = mFirebaseDatabase.getReference().child("quotes");

        //check and Install TTS
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean ttsInstalled = sharedPref.getBoolean("TTS", false);
        if(!ttsInstalled)
             checkTTS();


        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new FragmentAdapter(getFragmentManager(), list);
        viewPager.setAdapter(adapter);



        mDatabaseReference.orderByValue().limitToLast(7).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                quoteData =dataSnapshot.getValue(QuoteData.class);

                list.add(quoteData);
                adapter.notifyDataSetChanged();
                if(list.size() >=2)
                    //speaker.speak((list.get(1)).getQuote());
                Toast.makeText(MainActivity.this,quoteData.getQuote(),Toast.LENGTH_SHORT).show();

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
        }
);






//
//       tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    int result = tts.setLanguage(Locale.UK);
//                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Log.e("TTS", "This Language is not supported");
//                    }
//
//                    //speak(list);
//
//                } else {
//                    Log.e("TTS", "Initilization Failed!");
//                }
//            }
//        });



    }
//    private void speak(String text){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
//        }else{
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public void speakQuote(View view) {
        //fetch code and speak karo
        ImageView img = (ImageView) view;
        view.setClickable(false);
        ((AnimationDrawable) img.getBackground()).start();
    }

}

