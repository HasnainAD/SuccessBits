package com.hadilawar.successbits.FavoritesHub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hadilawar.successbits.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l1s14bscs2083 on 5/13/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private ArrayList<FavItem> mItems;
    private Context context;

    RVAdapter(ArrayList<FavItem> items, Context context) {
        mItems = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_fav_view, viewGroup, false);
        return new ViewHolder(v);

    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        FavItem item = mItems.get(i);
        Typeface reg = Typeface.createFromAsset(context.getAssets(), "fonts/lemonada-regular.ttf");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/Lemonada-Light.ttf");
        viewHolder.authorTextView.setText(item.getAuthor());
        viewHolder.authorTextView.setTypeface(reg);
        viewHolder.quoteTextView.setText(item.getQuote());
        viewHolder.quoteTextView.setTypeface(light);

//        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              Context context = view.getContext();
//              context.startActivity(new Intent(context, SecondActivity.class));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorTextView;
        private final TextView quoteTextView;

        ViewHolder(View v) {
            super(v);
            authorTextView = (TextView) v.findViewById(R.id.favauthor);
            quoteTextView = (TextView) v.findViewById(R.id.favquote);
        }
    }
}

