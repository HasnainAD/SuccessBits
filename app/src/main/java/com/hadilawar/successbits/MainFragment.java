package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import com.bumptech.glide.Glide;

import java.util.HashMap;

public class MainFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_LAYOUT="layout";
    private QuoteData quoteData;
    private Speaker speaker;
    private ImageView mImageView;
    private TextView mTextview;
    boolean speaking;
    private TextToSpeech tts;

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
        tts = new TextToSpeech(getActivity(), speaker);
        View fragmentView = inflater.inflate(getArguments().getInt(ARG_LAYOUT),container, false);
        mTextview = (TextView)fragmentView.findViewById(R.id.quotetext);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-regular.ttf");

        mTextview.setTypeface(typeface);
        mTextview.setLineSpacing(2f,0.7f);
       // mTextview.setLineSpaci;ineS
        final String text = (String)getArguments().get("quote") +"\n\t\t\t"+getArguments().get("author");
        mTextview.setText(text);
        speaking = false;

        Glide.with(this)
               .load(R.drawable.jet)
                .apply(circleCropTransform())
                .into((ImageView) fragmentView.findViewById(R.id.authorImage));

         ImageView share = (ImageView) fragmentView.findViewById(R.id.shareimage);

           share.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {

                   Toast.makeText(getActivity(), "In the zoning!", Toast.LENGTH_SHORT).show();
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                   sendIntent.setType("text/plain");
                   getActivity().startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
               }
           });
        mImageView = (ImageView) fragmentView.findViewById(R.id.speak);
        mImageView.setOnClickListener(this);
        final AnimationDrawable  am = (AnimationDrawable) mImageView.getBackground();

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                AnimationDrawable am = (AnimationDrawable) mImageView.getBackground();
               //mImageView.setImageResource(R.drawable.speaking);
                if(am!=null){
                   // am.start();
                    }
            }

            @Override
            public void onDone(String utteranceId) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.e("UI thread", "I am the UI thread");
                        mImageView.setImageResource(R.drawable.speak);
                        if(am!=null)
                          //  am.stop();
                        Toast.makeText(getActivity(),"hapakalo",Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        return(fragmentView);

    }

    @Override
    public void onClick(View v) {

        String quote = mTextview.getText().toString();
      //if false
        if(!speaking){
            speaking = true;
            speak(quote);
            mImageView.setImageResource(R.drawable.speaking);

        }else{
            tts.stop();
            speaking = false;
            mImageView.setImageResource(R.drawable.speak);

        }

    }

    public void speak(String text){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }


    /*private class SpeakQuote extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            mImageView.setImageResource(R.drawable.speaking);
            speaking = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            speaking = true;
            speaker.speak(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //((AnimationDrawable) mImageView.getBackground()).stop();
            mImageView.setImageResource(R.drawable.speak);
            speaking = false;
            super.onPostExecute(aVoid);
        }
    }*/

}
