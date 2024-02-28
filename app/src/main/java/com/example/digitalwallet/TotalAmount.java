package com.example.digitalwallet;

import android.content.Context;
import android.database.Cursor;

public class TotalAmount {

    private static int Income = 0;
    private static int Expense = 0;


    public int income(Context context){
        SQLite sqLite = new SQLite(context);
        Cursor cursor = sqLite.getShowData();

        int intk = 0;

        while (cursor.moveToNext()){

            String type = cursor.getString(1);
            int amount = cursor.getInt(4);

            if(type.equals("in")){
                intk = intk+amount;
            }
        }
        Income  = intk;
        return intk;
    }


    public int expense(Context context){
        SQLite sqLite = new SQLite(context);
        Cursor cursor = sqLite.getShowData();

        int extk = 0;

        while (cursor.moveToNext()){

            String type = cursor.getString(1);
            int amount = cursor.getInt(4);

            if(type.equals("ex")){
                extk = extk+amount;
            }
        }
        Expense = extk;
        return extk;
    }

    public int balance(Context context){

        int total = Income - Expense ;

        return total;
    }
}
