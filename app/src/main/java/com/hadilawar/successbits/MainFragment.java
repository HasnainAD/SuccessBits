package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_LAYOUT="layout";
    private QuoteData quoteData;
    private Speaker speaker;
    private ImageView mImageView;
    private TextView mTextview;
    boolean speaking;
    private SpeakQuote speakMe;


    //Returns an Instance of fragment
    static Fragment newInstance(int layoutId, QuoteData quoteData) {
        Fragment result=new MainFragment();
        //quoteData = quoteDat;
        Bundle args=new Bundle();
        args.putInt(ARG_LAYOUT, layoutId);
        args.putString("quote", quoteData.getQuote());
        args.putString("author", quoteData.getAuthorName());
        args.putString("imgurl", quoteData.getAuthorName());
        result.setArguments(args);
        return(result);
    }

    public MainFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        speaker = new Speaker(getActivity());
        View fragmentView = inflater.inflate(getArguments().getInt(ARG_LAYOUT),container, false);
        mTextview = (TextView)fragmentView.findViewById(R.id.quotetext);

        String text = (String)getArguments().get("quote") +"\n\t\t\t"+getArguments().get("author");
        mTextview.setText(text);
        speakMe = new SpeakQuote();
        speaking = false;
        mImageView = (ImageView) fragmentView.findViewById(R.id.speak);
        mImageView.setOnClickListener(this);
        return(fragmentView);

    }

    @Override
    public void onClick(View v) {

        String quote = mTextview.getText().toString();
        if(!(speakMe.getStatus() == AsyncTask.Status.RUNNING) ){


            ((AnimationDrawable) mImageView.getBackground()).start();
            Toast.makeText(getActivity(), "In the zoning!", Toast.LENGTH_SHORT).show();
            speakMe = new SpeakQuote();
            speakMe.execute(quote);

        }else {
            ((AnimationDrawable) mImageView.getBackground()).stop();
            speakMe.cancel(true);
        }
    }

    private class SpeakQuote extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {

            speaking = true;
            speaker.speak(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            ((AnimationDrawable) mImageView.getBackground()).stop();
            speaking = false;
            super.onPostExecute(aVoid);
        }
    }
}
