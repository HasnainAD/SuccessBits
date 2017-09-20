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



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean quoteExist(String quote){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery( "select * from "+ TableName + " where quote = ?", new String[]{quote});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean deleteContact(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ TableName + " where id = '"+ Integer.toString(ID)+"'";
        Log.e("QUERY", query);
        db.execSQL("DELETE FROM "+ TableName + " where id = '"+ Integer.toString(ID)+"'");
        db.close();
        return true;
    }
    public boolean deleteContact(String value){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "DELETE FROM "+ TableName + " where id = '"+ value+"'";
       // Log.e("QUERY", query);
        String query = "DELETE FROM "+ TableName + " where quote = \""+ value+"\"";
        Log.e("QUERY", query);
        db.execSQL(query);
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


