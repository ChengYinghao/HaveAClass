package com.cyh.haveaclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CYH on 2018/2/12.
 */

public class ClassScheduleDatabaseHelper extends SQLiteOpenHelper {
    private final String TABLE_NAME = "ClassScheduleTable";
    public ClassScheduleDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDataBase = "CREATE TABLE "+TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREAMENT, " +
                "time TEXT, site TEXT, whatDay TEXT, whatClass DATE/TIME, isOddWeek YES/NO);";
        db.execSQL(createDataBase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
