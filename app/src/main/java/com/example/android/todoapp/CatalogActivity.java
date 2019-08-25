package com.example.android.todoapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static int LOADER = 0;
    private nCursorAdapter cursorAdapter;
    private int a;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ListView ll = (ListView) findViewById(R.id.ll);
        TextView emptyText=(TextView)findViewById(R.id.emptyText);

        cursorAdapter =new nCursorAdapter(this, null);
        ll.setAdapter(cursorAdapter);

        //-------------- when there is no task to display------------------
        ll.setEmptyView(emptyText);
        emptyText.setText("Create your Todo list");

        getLoaderManager().initLoader(LOADER, null, this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        //  display();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                DeleteAll();
                //display();
                return true;
            case R.id.action_feedback:
                feedback();
                //display();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void feedback() {
        /*
        *insert dummy to test
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Entry.COLUMN_TASK, "CALL MOM");

        long rowId = sqLiteDatabase.insert(Contract.Entry.TABLE_NAME, null, contentValues);
        Toast.makeText(getApplicationContext(), "dummy inserted at " + rowId, Toast.LENGTH_SHORT).show();
        */
    }
    /*
        public void display() {
            ListView ll = (ListView) findViewById(R.id.ll);
            DbHelper mDbHelper = new DbHelper(this);
            SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Contract.Entry.TABLE_NAME, null);
            nCursorAdapter nCursorAdapter =new nCursorAdapter(this, cursor);
            ll.setAdapter(nCursorAdapter);
        }
    */
    public void DeleteAll() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getContentResolver().delete(Contract.Entry.CONTENT_URI,
                                null, null);
                        Toast.makeText(getApplicationContext(),"All tasks cleared"  , Toast.LENGTH_SHORT).show();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CatalogActivity.this);
        builder.setMessage("Do you want to DELETE ALL tasks?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        //DbHelper mDbHelper = new DbHelper(this);
        //SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        //sqLiteDatabase.delete(Contract.Entry.TABLE_NAME, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {Contract.Entry.COL_ID, Contract.Entry.COLUMN_TASK, Contract.Entry.COLUMN_TASK_STATUS};
        return new CursorLoader(this, Contract.Entry.CONTENT_URI, projection, null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
