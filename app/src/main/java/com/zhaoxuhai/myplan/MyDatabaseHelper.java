package com.zhaoxuhai.myplan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by zhaoxuhai on 15-9-11.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String createPlanTbl = "create table Plan ("
            + "planId integer primary key autoincrement, "
            + "planName text, "
            + "iconImg text, "
            + "iconColor text, "
            + "planUnit text)";
    public static final String createRecordTbl = "create table Record ("
            + " recordId integer primary key autoincrement, "
            + " num integer, "
            + " planId integer, "
            + " year integer, "
            + " month integer, "
            + " day integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPlanTbl);
        db.execSQL(createRecordTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
