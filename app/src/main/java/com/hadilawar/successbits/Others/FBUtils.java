package com.hadilawar.successbits.Others;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by l1s14bscs2083 on 7/22/2017.
 */

public class FBUtils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}