package com.example.android.todoapp;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

import static android.provider.BaseColumns._ID;

public class nCursorAdapter extends CursorAdapter {
    public nCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);
    }

//    private CheckBox checkBox;
    private LinearLayout linearLayout;
    private TextView rowIdTextView;
    private ImageView delImg;
    private String task, idValue;

    @Override
    public void bindView(View view, final Context context,  Cursor cursor) {

        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox1);
        linearLayout=(LinearLayout)view.findViewById(R.id.linearLayoutCatalogActivity);

        final TextView taskTextView = (TextView)view.findViewById(R.id.taskTextView);
        rowIdTextView = (TextView)view.findViewById(R.id.rowIdTextView);
        delImg = (ImageView)view.findViewById(R.id.delImg);

        //------getting value from database
        int taskColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_TASK);
        task = cursor.getString(taskColumnIndex);

        int idColumnIndex = cursor.getColumnIndex(Contract.Entry.COL_ID);
        idValue = cursor.getString(idColumnIndex);

        int checkBoxColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_TASK_STATUS);
        int checkBoxValue = cursor.getInt(checkBoxColumnIndex);

        taskTextView.setText(task);
        rowIdTextView.setText(idValue);

        final ListView listView = (ListView)view.findViewById(R.id.ll);

        if(checkBoxValue==1){
            checkBox.setChecked(true);
            taskTextView.setPaintFlags(taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setEnabled(false);

        }else {
            checkBox.setChecked(false);
            taskTextView.setPaintFlags(taskTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    //        checkBox.setEnabled(true);
        }
        Log.d("", "checkBoxValue: " + checkBoxValue + " for " + task);
        //Toast.makeText(context, "checkBoxValue: " + checkBoxValue+", for: " +task, Toast.LENGTH_LONG).show();

        final int rID = Integer.parseInt(rowIdTextView.getText().toString());

        //------------- checkbox ------------------
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    taskTextView.setPaintFlags(taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    //Toast.makeText(context, ""+ rID , Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Entry.COLUMN_TASK_STATUS, 1);
                    String a = Contract.Entry.COL_ID + "=?";
                    String[] b = new String[]{String.valueOf(rID)};

                    long row = context.getContentResolver().update(Contract.Entry.CONTENT_URI,
                            contentValues, a, b);
                    Log.d("", " updated to "+row);
            //        Toast.makeText(context, " updated to "+row , Toast.LENGTH_SHORT).show();

                } else {
                    taskTextView.setPaintFlags(taskTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Entry.COLUMN_TASK_STATUS, 0);
                    String a = Contract.Entry.COL_ID + "=?";
                    String[] b = new String[]{String.valueOf(rID)};

                    long row = context.getContentResolver().update(Contract.Entry.CONTENT_URI,
                            contentValues, a, b);
                    Log.d("", " updated to "+row);

                    //          Toast.makeText(context, " updated to "+row , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //----------------- Editing task text -----------------------------
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "value: "+ taskTextView.getText().toString()
  //                      +", row: "+ rID,Toast.LENGTH_SHORT).show();
                if(!checkBox.isChecked()) {
                    Intent intent = new Intent(context, EditSavedActivity.class);
                    intent.putExtra("value", taskTextView.getText().toString());
                    intent.putExtra("row value", rID);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "Task is done", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //----------------- Action on clicking on delete button -----------------------------
        delImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                //Toast.makeText(context,""+rID  , Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to DELETE the task?");
                //------------------ If selected Yes ------------------------
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View parentRow = (View) view.getParent();

                        context.getContentResolver().delete(Contract.Entry.CONTENT_URI,
                                Contract.Entry.COL_ID+"=?",
                                new String[]{String.valueOf(rID)});
                        Toast.makeText(context,"Task is removed "+rID  , Toast.LENGTH_SHORT).show();
                    }
/*                    private void delete() {
                        DbHelper mDbHelper = new DbHelper(context);
                        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
                        sqLiteDatabase.delete(Contract.Entry.TABLE_NAME, _ID + "=?",new String[]{});
                        //Toast.makeText(context,"Data removed from row " + rowNo, Toast.LENGTH_SHORT).show();
                    }*/
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(context,"No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}