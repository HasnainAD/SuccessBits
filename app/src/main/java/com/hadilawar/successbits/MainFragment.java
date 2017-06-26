package com.hadilawar.successbits;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment {
    private static final String ARG_LAYOUT="layout";

    //Returns an Instance of fragment
    static Fragment newInstance(int layoutId) {
        Fragment result=new MainFragment();
        Bundle args=new Bundle();
        args.putInt(ARG_LAYOUT, layoutId);
        result.setArguments(args);
        return(result);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return(inflater.inflate(getArguments().getInt(ARG_LAYOUT),container, false));
    }
}
