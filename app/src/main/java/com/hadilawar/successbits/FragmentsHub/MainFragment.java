package com.hadilawar.successbits.FragmentsHub;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hadilawar.successbits.DBHelper;
import com.hadilawar.successbits.FavoritesHub.FavItem;
import com.hadilawar.successbits.R;
import com.hadilawar.successbits.Others.Speaker;

import java.security.MessageDigest;
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
    private String aboutAuthor;
    private NotificationCompat.Builder mBuilder;
    private ImageView shareIcon;
    private ImageView nextImage;
    private ImageView prevImage;

    DBHelper dbHelper;
    private String quote;
    private  boolean quoteIsFaved = false;
    ViewPager mViewpager;

    //Returns an Instance of fragment
    static Fragment newInstance(int position, QuoteData quoteData) {
        Fragment result=new MainFragment();
        Bundle args=new Bundle();
        //args.putInt(ARG_LAYOUT, layoutId);
        args.putString("quote", quoteData.getQuote());
        args.putString("author", quoteData.getAuthorName());
        args.putString("imgurl", quoteData.getImageUrl());
        args.putString("aboutauthor", quoteData.getAboutAuthor());
        args.putInt("position", position);
        result.setArguments(args);
        return result;
    }

    public MainFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        speaker = new Speaker(getActivity());
        tts = new TextToSpeech(getActivity(), speaker);
        quote = (String)getArguments().get("quote");
        quoteText = "\""+getArguments().get("quote") +".\"";
        authorName = getArguments().getString("author");
        imageUrl = getArguments().getString("imgurl");
        aboutAuthor = getArguments().getString("aboutauthor");

        position = getArguments().getInt("position");

        speaking = false;
        mViewpager= (ViewPager) getActivity().findViewById(R.id.pager);


//
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment,container, false);
        mQuoteTextview = (TextView)fragmentView.findViewById(R.id.quotetext);
        mAboutTitleText =(TextView)fragmentView.findViewById(R.id.bottomsheettitle);
        mAboutText =     (TextView)fragmentView.findViewById(R.id.bottomsheettext);
        mAuthorNameText =(TextView)fragmentView.findViewById(R.id.authorName);
        mFavView =       (ImageView)fragmentView.findViewById(R.id.favimage);
        mImageView =     (ImageView)fragmentView.findViewById(R.id.speakimage);
        shareIcon =      (ImageView) fragmentView.findViewById(R.id.shareimage);

        mAuthorNameText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-regular.ttf"));
        mQuoteTextview.setTypeface(Typeface.createFromAsset(getActivity().getAssets() , "fonts/Lemonada-Light.ttf"));
        mAboutTitleText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/lemonada-semibold.ttf"));
        mAboutText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lemonada-Light.ttf"));

        //SET TEXT
        mQuoteTextview.setText(quoteText);
        mAuthorNameText.setText(authorName);

        mAboutText.setText(aboutAuthor);
        mAuthorNameText.setText(authorName);


        //ALL BOTTOMSHEETS MATERIAL
        bottomSheetImageView = (ImageView)fragmentView.findViewById(R.id.bottom_sheet_indicator);
        final View bottomSheet = (View) fragmentView.findViewById(R.id.bottom_sheet);

//        TextView bottomSheetTitle = (TextView) bottomSheet.findViewById(R.id.bottomsheettitle);
//        TextView bottomSheetText  = (TextView) bottomSheet.findViewById(R.id.bottomsheettext);
//


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
        //SET FAVORITE ICON
       new  CheckAndLoadFavImage().execute(quote);
        //FRAGMENT CHANGE LISTENER
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

        //DOWNLOADING WITH GLIDE
        //// TODO: 8/19/2017 SET URL HERE
        ////TODO: SET DEFAULT IMAGE, IF APPLICABLE
        Glide.with(this)
               .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .apply(circleCropTransform())
                 .into((ImageView) fragmentView.findViewById(R.id.authorImage));

        // SET AT START IF ITS FAVED
//        if(quoteIsFaved){
//            mFavView.setImageResource(R.drawable.faved);
//            Toast.makeText(getActivity(),"returns true", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            mFavView.setImageResource(R.drawable.favorite);
//            Toast.makeText(getActivity(),"returns false", Toast.LENGTH_SHORT).show();
//        }

        //SET ONCLICK LISTENER OF ALL ICONS
        shareIcon.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mFavView.setOnClickListener(this);

        //SPEECH START AND END LISTENER
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
                       // Toast.makeText(getActivity(),"hapakalo",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
        return(fragmentView);

    }
    private int getItem(int i) {
        return mViewpager.getCurrentItem() + i;
    }

    //THIS HANDLES ALL THE ONCLICKS IN A FRAGMENT
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.prevImage) {
            mViewpager.setCurrentItem(getItem(-1), true);
        }
        else if(v.getId() == R.id.nextImage){

            mViewpager.setCurrentItem(getItem(1), true);
        }

        else if (v.getId() == R.id.shareimage){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, quoteText+authorName);
            sendIntent.setType("text/plain");
            getActivity().startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }

       else if(v.getId() == R.id.speakimage) {
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
                //NOT FAVOURITE YET, MAKE IT ONE
                imageView.setImageDrawable(faved);
                new InsertTask().execute(new FavItem(authorName,quote,1));
            }else{
               //WAS FAVOURITE, UNDO IT
                imageView.setImageDrawable(favorite);
                new DeleteTask().execute(quote);
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

    //BACKGROUND THREAD FOR CHECKING WHETHER CURRENT QUOTE IS FAVORITE OR NOT
    private class CheckAndLoadFavImage extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //IF QUOTE FAVED
            if(aBoolean == Boolean.TRUE){
                mFavView.setImageResource(R.drawable.faved);
               // Toast.makeText(getActivity(),"Added to Favourites", Toast.LENGTH_SHORT).show();
            }else{//IF QUOTE NOT FAVED
                mFavView.setImageResource(R.drawable.favorite);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if(dbHelper.quoteExist(params[0])) {

                Log.e("THIS EXISTS", params[0]);

                return Boolean.TRUE;
            }
            else{
                Log.e("THIS DOES NOT EXIST", params[0]);

                return Boolean.FALSE;
            }
        }
    }

    //BACKGROUND THREAD FOR MAKING THE QUOTE FAVORITE
    private class InsertTask extends AsyncTask<FavItem,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //IF QUOTE FAVED
            Drawable faved = ResourcesCompat.getDrawable(getResources(), R.drawable.faved, null);
            if(aBoolean == Boolean.TRUE){
                mFavView.setImageDrawable(faved);
              //  Toast.makeText(getActivity(),"Added to Favourites", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(FavItem... params) {

            return dbHelper.insertContact(params[0].getAuthor(),params[0].getQuote());
        }
    }

    //BACKGROUND THREAD FOR MAKING THE QUOTE FAVOURITE
    private class DeleteTask extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //IF QUOTE FAVED
            Drawable favorite= ResourcesCompat.getDrawable(getResources(), R.drawable.favorite, null);
            if(aBoolean == Boolean.TRUE){
                mFavView.setImageDrawable(favorite);
              //  Toast.makeText(getActivity(),"Deleted From Favourites", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return dbHelper.deleteContact(params[0]);
        }
    }

}
