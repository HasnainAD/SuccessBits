package com.hadilawar.successbits.FavoritesHub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadilawar.successbits.DBHelper;
import com.hadilawar.successbits.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l1s14bscs2083 on 5/13/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private ArrayList<FavItem> mItems;
    private Context context;
    private DBHelper dbHelper;
    RVAdapter(ArrayList<FavItem> items, Context context, DBHelper dbHelper) {
        this.mItems = items;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_fav_view, viewGroup, false);
        return new ViewHolder(v);

    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final FavItem item = mItems.get(i);
        final int position = i;

        Typeface reg = Typeface.createFromAsset(context.getAssets(), "fonts/lemonada-regular.ttf");
        Typeface light = Typeface.createFromAsset(context.getAssets(), "fonts/Lemonada-Light.ttf");

        viewHolder.authorTextView.setText(item.getAuthor());
        viewHolder.authorTextView.setTypeface(reg);
        viewHolder.quoteTextView.setText(item.getQuote());
        viewHolder.quoteTextView.setTypeface(light);

        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("SENT VALUE", Integer.toString(item.getId()));
                dbHelper.deleteContact(item.getId());

                mItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mItems.size());
                 mItems = dbHelper.getAll();
                Snackbar.make(view, R.string.snackbar_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                         }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView authorTextView;
        private final TextView quoteTextView;
        private final ImageView deleteImageView;

        ViewHolder(View v) {
            super(v);
            authorTextView = (TextView) v.findViewById(R.id.favauthor);
            quoteTextView = (TextView) v.findViewById(R.id.favquote);
            deleteImageView = (ImageView) v.findViewById(R.id.favdelete);
        }
    }
}

