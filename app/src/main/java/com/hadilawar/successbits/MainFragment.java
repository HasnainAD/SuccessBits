package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.text.method.ScrollingMovementMethod;
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
    private ImageView mFavView;
    private ImageView bottomSheetUp;
    private TextView mQuoteTextview;
    private TextView mAboutTitleText;
    private TextView mAuthorNameText;
    private TextView mAboutText;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton speakFloatingButton;
    private boolean speaking;
    private TextToSpeech tts;

    //Returns an Instance of fragment
    static Fragment newInstance(int layoutId, QuoteData quoteData) {
        Fragment result=new MainFragment();
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

        mQuoteTextview = (TextView)fragmentView.findViewById(R.id.quotetext);
        mAboutTitleText =(TextView)fragmentView.findViewById(R.id.aboutauthortitle);
        mAboutText =(TextView)fragmentView.findViewById(R.id.aboutauthortext);
        mAuthorNameText = (TextView)fragmentView.findViewById(R.id.authorName);
        mFavView = (ImageView) fragmentView.findViewById(R.id.favimage);
        mImageView = (ImageView) fragmentView.findViewById(R.id.speakimage);


        speakFloatingButton = (FloatingActionButton) fragmentView.findViewById(R.id.speakfloatingbutton);

        Typeface typefac = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-regular.ttf");
        mAuthorNameText.setTypeface(typefac);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lemonada-Light.ttf");
        mQuoteTextview.setTypeface(typeface);
        //mTextview.setLineSpacing(2f,0.7f);

        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-semibold.ttf");
        mAboutTitleText.setTypeface(typeface2);

        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lemonada-Light.ttf");
        mAboutText.setTypeface(typeface3);
        mAboutText.setMovementMethod(new ScrollingMovementMethod());

       // mTextview.setLineSpaci;ineS
        final String text = "\""+(String)getArguments().get("quote") +".\"";
        mQuoteTextview.setText(text);
        speaking = false;
        bottomSheetUp = (ImageView)fragmentView.findViewById(R.id.bottom_sheet_indicator);

        View bottomSheet = (View) fragmentView.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(80);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                  // bottomSheetUp.setImageResource(R.drawable.up);
//                }
//                else if ( newState == BottomSheetBehavior.STATE_EXPANDED)
//                {
//                    //bottomSheetUp.setImageResource(R.drawable.down);
//                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

              //  Bottom Sheet moving up
                if(slideOffset > 0.5f) {
                    bottomSheetUp.setImageResource(R.drawable.down);
                }else {
                    bottomSheetUp.setImageResource(R.drawable.up);
                }
            }
        });




        Glide.with(this)
               .load(R.drawable.tony)
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

        mImageView.setOnClickListener(this);
        mFavView.setOnClickListener(this);

//        final AnimationDrawable  am = (AnimationDrawable) mImageView.getBackground();

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
                        //if(am!=null)
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

        if(v.getId() == R.id.speakimage) {
            String quote = mQuoteTextview.getText().toString();
            //if false
            if (!speaking) {
                speaking = true;
                speak(quote);
                mImageView.setImageResource(R.drawable.speaking);

            } else {
                tts.stop();
                speaking = false;
                mImageView.setImageResource(R.drawable.speak);

            }
        }

        else if (v.getId() == R.id.favimage){
            ImageView imageView = (ImageView)v;
             Drawable faved = ResourcesCompat.getDrawable(getResources(), R.drawable.faved, null);
             Drawable favorite= ResourcesCompat.getDrawable(getResources(), R.drawable.favorite, null);
            if(imageView.getDrawable().getConstantState().equals(favorite.getConstantState())){
                imageView.setImageDrawable(faved);
            }else{
                imageView.setImageDrawable(favorite);
            }
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
