package com.example.android.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

public class EditSavedActivity extends AppCompatActivity {

    private String taskToBeEdited;
    private EditText item;
    private int rowId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_saved);

        Intent intent = getIntent();
        rowId = intent.getExtras().getInt("row value");
        taskToBeEdited = intent.getExtras().getString("value");

        Log.d("", "row: "+ rowId+ ", task:" + taskToBeEdited);

        item = (EditText)findViewById(R.id.edit_task_EditSavedActivity);
        item.setText(taskToBeEdited);
    }
    //---------------edit existing task and update --------------------------------
    public void edit(){
        String task = item.getText().toString().trim();

        if(task.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter Task to update " , Toast.LENGTH_SHORT).show();
        }else if(!task.equals(taskToBeEdited)) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.Entry.COLUMN_TASK, task);

            String sele = Contract.Entry.COL_ID + "=?";
            String[] seleArg = new String[]{String.valueOf(rowId)};

            long rID = getContentResolver().update(Contract.Entry.CONTENT_URI, cv, sele, seleArg);
            Toast.makeText(getApplicationContext(), "Task is updated " , Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "No changes to update ", Toast.LENGTH_SHORT).show();
            finish();
        }
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
                edit();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
