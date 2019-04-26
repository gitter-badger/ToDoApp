package com.example.android.todoapp;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insert();
                finish();
                return true;

            case R.id.action_delete:
                finish();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void insert(){
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

        EditText item = (EditText)findViewById(R.id.edit_task);

        String fName = item.getText().toString().trim();

        ContentValues cv = new ContentValues();
        cv.put(Contract.Entry.COLUMN_TASK, fName);

        long rID = sqLiteDatabase.insert(Contract.Entry.TABLE_NAME, null, cv);
        Toast.makeText(getApplicationContext(), fName + " is inserted at "+ rID, Toast.LENGTH_SHORT).show();
    }
}