package com.zhaoxuhai.myplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class LineActivity extends Activity implements View.OnClickListener {

    private int planId;
    private MyDatabaseHelper dbHelper;
    private int version = 1;
    private int year;
    private int month;
    private int daysCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_line);

        dbHelper = new MyDatabaseHelper(this, "MyPlan.db", null, version);
        Intent intent = getIntent();
        planId = intent.getIntExtra("planId", 0);
        Calendar cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        //putData();
        getData();

    }

//    public void putData() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{12, 1, 2015, 10, 2});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{59, 1, 2015, 10, 3});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{3, 1, 2015, 10, 5});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{160, 1, 2015, 10, 7});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{100, 1, 2015, 10, 11});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{120, 1, 2015, 10, 13});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{60, 1, 2015, 10, 17});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{40, 1, 2015, 10, 19});
//        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
//                new Object[]{245, 1, 2015, 10, 23});
//    }

    public Map getData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select day,num from Record where planId="+planId+" and year="+year+" and month="+month, null);
        Map map = new HashMap();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            int num = cursor.getInt(cursor.getColumnIndex("num"));
            map.put(day, num);
        }
        cursor.close();
        Calendar myCal = new GregorianCalendar(year, month, 1);
        daysCount = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=1;i<=daysCount;i++) {
            if(map.containsKey(i) == false) {
                map.put(i, 0);
            }
        }
//        for(int j=1;j<=daysCount;j++) {
//            Log.d("num count", j+" "+map.get(j));
//        }
        return map;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_line, menu);
        return true;
    }

    public static void actionStart(Context context, int planId) {
        Intent intent = new Intent(context, LineActivity.class);
        intent.putExtra("planId", planId);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
