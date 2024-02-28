package com.example.digitalwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {

   private static String DB_NAME="Wallet";
    private static int VERSION=1;
    public SQLite(@Nullable Context context ) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table my_table(Id INTEGER primary key autoincrement, Type TEXT ,Title TEXT, Date TEXT , Mony INTEGER)");
        }catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table if exists my_table");
        }catch (Exception e){

        }

    }
    public void getInsertData(String type,String title, String date , int mony){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Type",type);
        values.put("Title",title);
        values.put("Date",date);
        values.put("Mony",mony);
        sqLiteDatabase.insert("my_table",null,values);
    }

    public Cursor getShowData(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from my_table ORDER BY Id DESC",null);

        return cursor;

    }
}
