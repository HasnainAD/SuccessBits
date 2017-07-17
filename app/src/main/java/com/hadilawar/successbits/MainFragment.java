package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainFragment extends Fragment {
    private static final String ARG_LAYOUT="layout";
    private QuoteData quoteData;

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

        View fragmentView = inflater.inflate(getArguments().getInt(ARG_LAYOUT),container, false);
        TextView textview = (TextView)fragmentView.findViewById(R.id.quotetext);
        String text = (String)getArguments().get("quote") +"\n\t\t\t"+getArguments().get("author");
        textview.setText(text);
        return(fragmentView);
    }
}
