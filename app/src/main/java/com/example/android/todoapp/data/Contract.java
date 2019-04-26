package com.example.android.todoapp.data;


import android.provider.BaseColumns;

public class Contract {
    public Contract(){
    }

    public static final class Entry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;

        public static final String TABLE_NAME = "taskTable";

        public static final String COLUMN_TASK = "task";
    }
}
