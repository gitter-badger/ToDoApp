package com.example.android.todoapp;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.todoapp.data.Contract;
import com.example.android.todoapp.data.DbHelper;
import com.example.android.todolistapp.R;

public class nCursorAdapter extends CursorAdapter {
    public nCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final String rowNo = cursor.getString(cursor.getColumnIndex(Contract.Entry._ID));

        final TextView a = (TextView)view.findViewById(R.id.name);

        int nameColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_TASK);
        String name = cursor.getString(nameColumnIndex);

        a.setText(name);

        final CheckBox cb = (CheckBox)view.findViewById(R.id.c);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()) {
                    a.setPaintFlags(a.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    a.setPaintFlags(a.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        final ImageView img = (ImageView)view.findViewById(R.id.summary);

        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to DELETE the task?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                        //notifyDataSetChanged();
                        //                      Toast.makeText(context,"Task deleted", Toast.LENGTH_SHORT).show();
                    }

                    private void delete() {

                        DbHelper mDbHelper = new DbHelper(context);
                        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

                        sqLiteDatabase.delete(Contract.Entry.TABLE_NAME,Contract.Entry._ID + "=?",new String[]{rowNo});
                        //Toast.makeText(context,"Data removed from row " + rowNo, Toast.LENGTH_SHORT).show();


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(context,"No Button Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}