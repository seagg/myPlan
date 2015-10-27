package com.zhaoxuhai.myplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;


public class AddCountActivity extends Activity implements View.OnClickListener {

    private Button add1Btn = null;
    private Button minus1Btn = null;
    private Button add10Btn = null;
    private Button minus10Btn = null;
    private Button add50Btn = null;
    private Button minus50Btn = null;
    private Button okBtn = null;
    private TextView planNameTxt = null;
    private SeekBar currCountBar = null;
    private TextView currCountShow = null;

    private TextView totalCountShow = null;
    private TextView maxCountShow = null;
    private TextView aveCountShow = null;

    private String planName = "";
    private String planUnit = "";
    private int planId;
    private int version = 2;

    private int totalCount = 0;
    private float averageCount = 0.0f;
    private int maxCount = 0;

    private MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addcount);

        Intent intent = getIntent();
        planName = intent.getStringExtra("planName");
        planUnit = intent.getStringExtra("planUnit");
        planId = intent.getIntExtra("planId", 0);



        add1Btn = (Button) findViewById(R.id.add1);
        minus1Btn = (Button) findViewById(R.id.minus1);
        add10Btn = (Button) findViewById(R.id.add10);
        minus10Btn = (Button) findViewById(R.id.minus10);
        add50Btn = (Button) findViewById(R.id.add50);
        minus50Btn = (Button) findViewById(R.id.minus50);
        okBtn = (Button) findViewById(R.id.ok_btn);
        currCountBar = (SeekBar) findViewById(R.id.curr_count_bar);
        currCountShow = (TextView) findViewById(R.id.curr_count_show);
        planNameTxt = (TextView) findViewById(R.id.plan_name);
        planNameTxt.setText(planName);

        totalCountShow = (TextView) findViewById(R.id.total_count);
        maxCountShow = (TextView) findViewById(R.id.max_count);
        aveCountShow = (TextView) findViewById(R.id.average_count);

        dbHelper = new MyDatabaseHelper(this, "MyPlan.db", null, version);

        add1Btn.setOnClickListener(this);
        minus1Btn.setOnClickListener(this);
        add10Btn.setOnClickListener(this);
        minus10Btn.setOnClickListener(this);
        add50Btn.setOnClickListener(this);
        minus50Btn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        getStatisticInfo();

        totalCountShow.setText("总数:"+totalCount);
        maxCountShow.setText("最大值:"+maxCount);
        aveCountShow.setText("平均值:"+averageCount);



        currCountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currCountShow.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static void actionStart(Context context, int planId, String planName, String planUnit) {
        Intent intent = new Intent(context, AddCountActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("planName", planName);
        intent.putExtra("planUnit", planUnit);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add1:
                currCountBar.setProgress(currCountBar.getProgress()+1);
                //currCountShow.setText( String.valueOf(Integer.valueOf(currCountShow.getText().toString())+1));
                break;
            case R.id.minus1:
                currCountBar.setProgress(currCountBar.getProgress()-1);
                //currCountShow.setText(String.valueOf(Integer.valueOf(currCountShow.getText().toString())-1));
                break;
            case R.id.add10:
                currCountBar.setProgress(currCountBar.getProgress()+10);
                //currCountShow.setText(String.valueOf(Integer.valueOf(currCountShow.getText().toString())+10));
                break;
            case R.id.minus10:
                currCountBar.setProgress(currCountBar.getProgress()-10);
                //currCountShow.setText(String.valueOf(Integer.valueOf(currCountShow.getText().toString())-10));
                break;
            case R.id.add50:
                currCountBar.setProgress(currCountBar.getProgress()+50);
                //currCountShow.setText(String.valueOf(Integer.valueOf(currCountShow.getText().toString())+50));
                break;
            case R.id.minus50:
                currCountBar.setProgress(currCountBar.getProgress()-50);
                //currCountShow.setText(String.valueOf(Integer.valueOf(currCountShow.getText().toString())-50));
                break;
            case R.id.ok_btn:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (currCountBar.getProgress() > 0) {
                    Calendar cal=Calendar.getInstance();
                    int year=cal.get(Calendar.YEAR);
                    int month=cal.get(Calendar.MONTH)+1;
                    int day=cal.get(Calendar.DAY_OF_MONTH);
                    int num=0;
                    Cursor cursor = db.rawQuery("select num from Record where planId="+planId+" and year="+year+" and month="+month+" and day="+day, null);
                    if( cursor.getCount() !=0 ) {
                        cursor.moveToFirst();
                        num = cursor.getInt(cursor.getColumnIndex("num"));
                        Log.d("planId", planId+"");
                        Log.d("already num", num+"");
                        cursor.close();
                        num = num + currCountBar.getProgress();
                        db.execSQL("update Record set num=? where planId=? and year=? and month=? and day=?",
                                new Object[]{num, planId, year, month, day});
                    } else {
                        db.execSQL("insert into Record(num, planId, year, month, day) values(?,?,?,?,?)",
                                new Object[]{currCountBar.getProgress(), planId, year, month, day});
                    }
                }
                LineActivity.actionStart(AddCountActivity.this, planId);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_count, menu);
        return true;
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

    public void getStatisticInfo() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select num from Record where planId="+planId, null);
        int tmp_num = 0;
        int count = 0;
        while (cursor.moveToNext()) {
            tmp_num = cursor.getInt(cursor.getColumnIndex("num"));
            totalCount += tmp_num;
            if(tmp_num > maxCount) {
                maxCount = tmp_num;
            }
            count++;
        }
        cursor.close();
        averageCount = (float)(Math.round(totalCount/count*10))/10;
    }
}
