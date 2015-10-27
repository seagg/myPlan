package com.zhaoxuhai.myplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class LineActivity extends Activity implements View.OnClickListener {

    private int planId;
    private MyDatabaseHelper dbHelper;
    private int version = 2;
    private int year;
    private int month;
    private int daysCount;

    private Button backBtn = null;
    private Button beforeBtn = null;
    private Button afterBtn = null;
    private TextView yearMonthShow = null;

    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_line);

        backBtn = (Button)findViewById(R.id.back_btn);
        beforeBtn = (Button)findViewById(R.id.before_btn);
        afterBtn = (Button)findViewById(R.id.after_btn);
        yearMonthShow = (TextView)findViewById(R.id.year_month_show);

        backBtn.setOnClickListener(this);
        beforeBtn.setOnClickListener(this);
        afterBtn.setOnClickListener(this);

        chart = (LineChart)findViewById(R.id.chart);
        dbHelper = new MyDatabaseHelper(this, "MyPlan.db", null, version);
        Intent intent = getIntent();
        planId = intent.getIntExtra("planId", 0);
        Calendar cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
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
        yearMonthShow.setText(year+"-"+month);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select day,num from Record where planId="+planId+" and year="+year+" and month="+month, null);
        Map<Integer, Integer> map = new HashMap<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            int num = cursor.getInt(cursor.getColumnIndex("num"));
            map.put(day, num);
        }
        cursor.close();
        Calendar myCal = new GregorianCalendar(year, month, 1);
        daysCount = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        xVals=new ArrayList<>();
        yVals=new ArrayList<>();

        for(int i=1;i<=daysCount;i++) {
            xVals.add(i+"");
            int y = 0;
            if(map.containsKey(i) == true) {
                y=map.get(i);
            }
            yVals.add(new Entry(y,i-1));
        }
        dataSet=new LineDataSet(yVals,"个数");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        chart.setData(data);
        chart.setDescription("数量统计");
        chart.animateY(1000);
        return map;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                Intent intent = new Intent(LineActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.before_btn:
                if(month >=2) {
                    month -= 1;
                } else {
                    month = 12;
                    year -= 1;
                }
                getData();
                break;
            case R.id.after_btn:
                if(month<=11) {
                    month += 1;
                } else {
                    month = 1;
                    year += 1;
                }
                getData();
                break;
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
