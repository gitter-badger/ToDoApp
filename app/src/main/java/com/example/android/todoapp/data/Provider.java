package com.example.android.todoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class Provider extends ContentProvider {

    private static final int TODO = 100;
    private static final int TODO_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String LOG_TAG = "";

    static {
        sUriMatcher.addURI(Contract.Entry.CONTENT_AUTHORITY, Contract.Entry.PATH_TODO, TODO);
        sUriMatcher.addURI(Contract.Entry.CONTENT_AUTHORITY, Contract.Entry.PATH_TODO + "/#", TODO_ID);
    }

    private DbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;

        SQLiteDatabase mSQLiteDatabase = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case TODO:
                cursor = mSQLiteDatabase.query(Contract.Entry.TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            case TODO_ID:
                s = Contract.Entry.COL_ID + "=?";
                strings1 = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = mSQLiteDatabase.query(Contract.Entry.TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:

                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertPet(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(Contract.Entry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:
                // Delete all rows that match the selection and selection args
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.Entry.TABLE_NAME, s, strings);
            case TODO_ID:
                // Delete a single row given by the ID in the URI
                s = Contract.Entry.COL_ID + "=?";
                strings = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.Entry.TABLE_NAME, s, strings);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:
                return updatePet(uri, contentValues, s, strings);
            case TODO_ID:
                s = Contract.Entry.COL_ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(Contract.Entry.TABLE_NAME, values, selection, selectionArgs);
    }
}