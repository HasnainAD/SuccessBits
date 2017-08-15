package com.hadilawar.successbits.FragmentsHub;

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
import android.support.v4.view.ViewPager;
import android.support.v7.app.NotificationCompat;
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
import com.hadilawar.successbits.R;
import com.hadilawar.successbits.Others.Speaker;

import java.util.HashMap;

public class MainFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_LAYOUT="layout";
    private QuoteData quoteData;
    private Speaker speaker;
    private ImageView mImageView;
    private ImageView mFavView;
    private ImageView bottomSheetImageView;
    private TextView mQuoteTextview;
    private TextView mAboutTitleText;
    private TextView mAuthorNameText;
    private TextView mAboutText;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton speakFloatingButton;
    private boolean speaking;
    private TextToSpeech tts;
    private int position;
    private String imageUrl;
    private String quoteText;
    private String authorName;
    private NotificationCompat.Builder mBuilder;


    //Returns an Instance of fragment
    static Fragment newInstance(int position, QuoteData quoteData) {
        Fragment result=new MainFragment();
        Bundle args=new Bundle();
        //args.putInt(ARG_LAYOUT, layoutId);
        args.putString("quote", quoteData.getQuote());
        args.putString("author", quoteData.getAuthorName());
        args.putString("imgurl", quoteData.getImageUrl());
        args.putInt("position", position);
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
        View fragmentView = inflater.inflate(R.layout.fragment,container, false);
        ViewPager mViewpager= (ViewPager) getActivity().findViewById(R.id.pager);

        quoteText = "\""+(String)getArguments().get("quote") +".\"";
        authorName = getArguments().getString("author");
        imageUrl = getArguments().getString("imgurl");
        position = getArguments().getInt("position");
        speaking = false;

        mQuoteTextview = (TextView)fragmentView.findViewById(R.id.quotetext);
        mAboutTitleText =(TextView)fragmentView.findViewById(R.id.aboutauthortitle);
        mAboutText =     (TextView)fragmentView.findViewById(R.id.aboutauthortext);
        mAuthorNameText =(TextView)fragmentView.findViewById(R.id.authorName);

        mFavView =       (ImageView)fragmentView.findViewById(R.id.favimage);
        mImageView =     (ImageView)fragmentView.findViewById(R.id.speakimage);

        Typeface typefac = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-regular.ttf");
        mAuthorNameText.setTypeface(typefac);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lemonada-Light.ttf");
        mQuoteTextview.setTypeface(typeface);
        //mTextview.setLineSpacing(2f,0.7f);

        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-semibold.ttf");
        mAboutTitleText.setTypeface(typeface2);

        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lemonada-Light.ttf");
        mAboutText.setTypeface(typeface3);
      //  mAboutText.setMovementMethod(new ScrollingMovementMethod());


        mQuoteTextview.setText(quoteText);

        bottomSheetImageView = (ImageView)fragmentView.findViewById(R.id.bottom_sheet_indicator);

        final View bottomSheet = (View) fragmentView.findViewById(R.id.bottom_sheet);
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
                    bottomSheetImageView.setImageResource(R.drawable.down);
                }else {
                    bottomSheetImageView.setImageResource(R.drawable.up);
                }
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING){

                    if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    }

                }
            }
        });
        Glide.with(this)
               .load(R.drawable.tony)
                .apply(circleCropTransform())
                .into((ImageView) fragmentView.findViewById(R.id.authorImage));

         ImageView shareIcon = (ImageView) fragmentView.findViewById(R.id.shareimage);

           shareIcon.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   Toast.makeText(getActivity(), "In the zoning!", Toast.LENGTH_SHORT).show();
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, quoteText+authorName);
                   sendIntent.setType("text/plain");
                   getActivity().startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
               }
           });

        mImageView.setOnClickListener(this);
        mFavView.setOnClickListener(this);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
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

    private void speak(String text){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

}
