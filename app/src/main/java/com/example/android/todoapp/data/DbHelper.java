package com.example.android.todoapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sDatabase";
    private static final int DATABASE_VERSION = 1;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlTable = "CREATE TABLE " + Contract.Entry.TABLE_NAME + " ( " +
                Contract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Entry.COLUMN_TASK + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(sqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
