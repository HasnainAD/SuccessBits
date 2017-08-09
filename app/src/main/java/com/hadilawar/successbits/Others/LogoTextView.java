package com.hadilawar.successbits.Others;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by l1s14bscs2083 on 7/29/2017.
 */

public class LogoTextView extends android.support.v7.widget.AppCompatTextView {

        public LogoTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public LogoTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public LogoTextView(Context context) {
            super(context);
            init();
        }

        public void init() {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Pacifico-Regular.ttf");
            setTypeface(tf ,1);

        }

}
