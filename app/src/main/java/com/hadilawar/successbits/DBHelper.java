package com.hadilawar.successbits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hadilawar.successbits.FavoritesHub.FavItem;

import java.util.ArrayList;

/**
 * Created by l1s14bscs2083 on 7/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DBname = "SuccessData";
    private static final String TableName = "favquotes";
    private static final String AuthorColumn = "author";
    private static final String QuoteColumn= "quote";
    public DBHelper(Context context){
        super(context, DBname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TableName + " "+
                        "(id integer primary key AUTOINCREMENT, author text, quote text)"
        );

        ContentValues contentValues = new ContentValues();
        contentValues.put(AuthorColumn, "Tony Robbins");
        contentValues.put(QuoteColumn, "What makes you comfortable can ruin you, what makes you uncomfortable is the only way to grow");
        db.insert(TableName, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(AuthorColumn, "Maxwell johns");
        contentValues2.put(QuoteColumn, "I need to move then I will began to feel the movement");
        db.insert(TableName, null, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(AuthorColumn, "Maxwell johns");
        contentValues3.put(QuoteColumn, "I need to move and then I will began to feel the movement");
        db.insert(TableName, null, contentValues3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean deleteContact(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ TableName + " where id = '"+ Integer.toString(ID)+"'";
        Log.e("QUERY", query);
        db.execSQL("DELETE FROM "+ TableName + " where id = '"+ Integer.toString(ID)+"'");
        db.close();
        return true;
    }

    public boolean insertContact (String author, String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AuthorColumn, author);
        contentValues.put(QuoteColumn, quote);
        db.insert(TableName, null, contentValues);
        db.close();
        return true;
    }
    public ArrayList<FavItem> getAll()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TableName, null );
        return CursorToArray(cursor);
    }

    private ArrayList<FavItem> CursorToArray(Cursor res)
    {
        ArrayList array = new ArrayList<FavItem>();
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array.add(new FavItem(
                                res.getString(res.getColumnIndex("author")),
                                res.getString(res.getColumnIndex("quote")),
                                res.getInt(res.getColumnIndex("id"))));
            Log.e("ID VALUE :: ",Integer.toString(res.getColumnIndex("id")));
            Log.e("AUTHOR VALUE :: ",res.getString(res.getColumnIndex("author")));
            Log.e("QUOTE VALUE :: ",res.getString(res.getColumnIndex("quote")));
            res.moveToNext();
        }
        return array;
    }
}


