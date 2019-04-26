package com.example.android.todoapp;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        display();
    }

    @Override
    public void onStart() {
        super.onStart();
        display();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data:
                insertDummy();
                display();
                return true;

            case R.id.action_delete_all_entries:
                DeleteAll();
                display();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertDummy() {
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Entry.COLUMN_TASK, "CALL MOM");

        long rowId = sqLiteDatabase.insert(Contract.Entry.TABLE_NAME, null, contentValues);
        Toast.makeText(getApplicationContext(), "dummy inserted at " + rowId, Toast.LENGTH_SHORT).show();
    }

    public void display() {
//        TextView textView = (TextView) findViewById(R.id.ll);
        ListView ll = (ListView) findViewById(R.id.ll);

        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Contract.Entry.TABLE_NAME, null);

        final nCursorAdapter nCursorAdapter =new nCursorAdapter(this, cursor);
        ll.setAdapter(nCursorAdapter);

    }
    public void DeleteAll() {
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

        sqLiteDatabase.delete(Contract.Entry.TABLE_NAME, null, null);
    }
}
