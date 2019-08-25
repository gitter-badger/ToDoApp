package com.example.android.todoapp.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public Contract(){
    }

    public static final class Entry implements BaseColumns {

        public static final String COL_ID = BaseColumns._ID;

        public static final String TABLE_NAME = "taskTable";

        public static final String COLUMN_TASK = "task";
        public static final String COLUMN_TASK_STATUS = "DONE";


        public static final String CONTENT_AUTHORITY = "com.example.android.todolistapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +CONTENT_AUTHORITY);
        public static final String PATH_TODO = "todolistapp";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODO);
    }
}
