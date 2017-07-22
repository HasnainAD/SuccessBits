package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

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

        final String text = (String)getArguments().get("quote") +"\n\t\t\t"+getArguments().get("author");
        mTextview.setText(text);
        speakMe = new SpeakQuote();
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
